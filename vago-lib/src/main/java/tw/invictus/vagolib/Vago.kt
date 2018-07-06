package tw.invictus.vagolib

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import tw.invictus.vagolib.VagoLog.ERROR_TAG
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaConstructor
import kotlin.reflect.jvm.jvmErasure

/**
 * Vago helps you write tests based on POJOs and supports test generation for POJOs transformations.
 *
 * Created by ivan on 2018/6/20.
 */
object Vago {
    private const val STRING = "test string"
    private const val LONG = 0L
    private const val SHORT = 0.toShort()
    private const val INT = 0
    private const val BOOLEAN = true
    private const val DOUBLE = 1.0
    private const val FLOAT = 1.0F
    private const val CHAR = 'A'

    private const val TRANS_PARAMS_SIZE = 1

    fun testClass(kClass: KClass<*>, customization: Customization? = null) {
        val props = Introspector.getBeanInfo(kClass.java).propertyDescriptors

        nextProp@ for (prop in props) {
            val skip = customization?.isSkipAttribute(prop.name) ?: false
            val getter = prop.readMethod
            val setter = prop.writeMethod
            if (skip || getter == null || setter == null) {
                VagoLog.l("Skip ${prop.name}. Condition: $skip || $getter == null || $setter == null.")
                continue@nextProp
            }

            val returnType = getter.returnType
            val params = setter.parameterTypes
            if (params.size != 1 || params[0] != returnType) {
                VagoLog.l("No valid setter for testing ${prop.name}. Condition: ${params.size} != 1 || ${params[0]} != $returnType.")
                continue@nextProp
            }

            try {
                // Try to get default test value by attribute name first, or get it by class.
                val value = customization?.getDefaultValueForSpecificAttribute(prop.name)
                        ?: getClassDefaultValue(returnType, customization)
                val instance = createInstance(kClass, customization)
                setter.invoke(instance, value)
                val actualValue = getter.invoke(instance)

                VagoLog.l("Testing property ${prop.name}, stub=$value, actualValue=$actualValue.")
                assertEquals("$ERROR_TAG Failed while testing property ${prop.name}.", value, actualValue)
            } catch (ex: Exception) {
                fail("An exception was thrown while testing ${prop.name}: exception = ${ex.message}.")
            }
        }
    }

    fun testTransformation(className: String, methodName: String, returnClassPath: String, packageName: String, customization: Customization? = null) {
        val staticJavaClazz = Class.forName("$packageName.$className")
        val returnClazz = Class.forName(returnClassPath)
        val clazz = getClassFromFirstMethodParameter(staticJavaClazz, methodName)
        val instance = clazz?.newInstance()
        val props = Introspector.getBeanInfo(clazz).propertyDescriptors

        nextProp@ for (prop in props) {
            val skip = customization?.isSkipAttribute(prop.name) ?: false
            val getter = prop.readMethod
            val setter = prop.writeMethod
            if (skip || getter == null || setter == null) {
                continue@nextProp
            }

            val returnType = getter.returnType
            val params = setter.parameterTypes
            if (params.size != 1 || params[0] != returnType) {
                continue@nextProp
            }

            try {
                val value = getClassDefaultValue(returnType, customization)
                val method = staticJavaClazz.methods.find { it.name == methodName }
                if (method == null) {
                    VagoLog.w("The $methodName method in $className was not found! Skipping this prop...")
                    continue@nextProp
                }
                setter.invoke(instance, value)
                val returnInstance = method.invoke(null, instance)
                val returnProps = Introspector.getBeanInfo(returnClazz).propertyDescriptors
                val field = clazz?.getDeclaredField(prop.name)
                findAndCheckReturnProps(returnProps, prop, field, value, returnInstance, getter.returnType, customization)
            } catch (ex: Exception) {
                fail("An exception was thrown while testing ${prop.name}: exception = ${ex.message}")
            }
        }
    }

    private fun findAndCheckReturnProps(returnProps: Array<PropertyDescriptor>, prop: PropertyDescriptor,
                                        field: Field?, value: Any?, returnInstance: Any?, returnType: Class<*>,
                                        customization: Customization?) {
        val annotation = field?.getAnnotation(VagoMapping::class.java)
        returnProps
                .filter { it.name == prop.name || it.name == annotation?.name }
                .forEach {
                    val returnGetter = it.readMethod
                    val actualValue = returnGetter.invoke(returnInstance)
                    if (annotation != null && annotation.type != returnType && value != null) {
                        VagoLog.l("Testing property ${prop.name} (${it.name}), stub=$value, actualValue=$actualValue.")
                        val convertedValue = VagoTypeConverter.convert(returnType, returnGetter.returnType, value, customization)
                        val errorMsg = "$ERROR_TAG Failed while testing property ${prop.name} (${it.name})."
                        assertEquals(errorMsg, convertedValue, actualValue)
                    } else {
                        VagoLog.l("Testing property ${prop.name}, stub=$value, actualValue=$actualValue.")
                        val errorMsg = "$ERROR_TAG Failed while testing property ${prop.name}."
                        assertEquals(errorMsg, value, actualValue)
                    }
                }
    }

    fun createInstance(kClass: KClass<*>, customization: Customization? = null): Any? {
        val argList = arrayListOf<Any>()
        val constructor = kClass.primaryConstructor?.javaConstructor
        kClass.primaryConstructor?.parameters?.map {
            val value = customization?.getDefaultValueForSpecificAttribute(it.name!!)
                    ?: getClassDefaultValue(it.type.jvmErasure.java, customization)
            value?.let { argList.add(it) }
        }
        val array = argList.toArray()
        return constructor?.newInstance(*array)
    }

    private fun getClassFromFirstMethodParameter(clazz: Class<*>, methodName: String): Class<*>? {
        clazz.methods.forEach {
            if (it.name == methodName && it.parameterTypes.size == TRANS_PARAMS_SIZE) {
                return it.parameterTypes.first()
            }
        }
        return null
    }

    private fun getClassDefaultValue(clazz: Class<*>, customization: Customization?): Any? {
        var result: Any? = customization?.getDefaultValueByClass(clazz)

        if (result != null) {
            return result
        }
        when {
            clazz.isString() ->
                result = STRING
            clazz.isArray ->
                result = arrayOf(clazz.componentType, 1)
            clazz.isBoolean() ->
                result = BOOLEAN
            clazz.isInt() ->
                result = INT
            clazz.isLong() ->
                result = LONG
            clazz.isDouble() ->
                result = DOUBLE
            clazz.isFloat() ->
                result = FLOAT
            clazz.isChar() ->
                result = CHAR
            clazz.isShort() ->
                result = SHORT
            clazz.isEnum ->
                result = clazz.enumConstants[0]
            clazz.isList() ->
                result = emptyList<Any>()
            else -> {
                VagoLog.w("${clazz.canonicalName} is not in default values.")
            }
        }

        return result
    }

    interface Customization {
        fun getDefaultValueByClass(clazz: Class<*>): Any?
        fun getDefaultValueForSpecificAttribute(attr: String): Any?
        fun isSkipAttribute(attr: String): Boolean
        fun getConvertedType(originalType: Class<*>, wantedType: Class<*>): Any?
    }

    open class VagoCustomization : Customization {
        /*
         * Get Default Value by Class.
         * For example, if the class is String, return a default string "test string".
         */
        override fun getDefaultValueByClass(clazz: Class<*>): Any? = null

        /*
         * Get Default Value of Specific Attribute
         * For example, if the class has an attribute called "id", return a Long with 0L value.
         */
        override fun getDefaultValueForSpecificAttribute(attr: String): Any? = null

        /*
         * Skip Attributes that you do not want to test
         */
        override fun isSkipAttribute(attr: String) = false

        /*
         * Get Custom Convert type between different classes
         */
        override fun getConvertedType(originalType: Class<*>, wantedType: Class<*>): Any? = null
    }
}
