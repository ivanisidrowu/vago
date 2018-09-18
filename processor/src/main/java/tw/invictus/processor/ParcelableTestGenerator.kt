package tw.invictus.processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import org.jetbrains.annotations.Nullable
import tw.invictus.processor.VagoProcessor.Companion.VAGO_CLASS
import tw.invictus.processor.VagoProcessor.Companion.VAGO_CUSTOM_PARAM
import tw.invictus.processor.VagoProcessor.Companion.VAGO_TEST_METHOD_PREFIX
import tw.invictus.vagolib.Vago
import javax.lang.model.element.Modifier.*

class ParcelableTestGenerator(
        private val contents: List<ParcelableTestContent>
): VagoGenerator {

    companion object {
        const val PARCEL_NAME = "Parcelable"
        const val CLASS_NAME = VAGO_CLASS + PARCEL_NAME
        const val PACKAGE_NAME = "tw.invictus.vago"
    }

    override fun generate(): TypeSpec {
        val typeBuilder = TypeSpec
                .classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL)
        contents.forEach{
            typeBuilder.addMethod(generateMethod(it.packageName, it.className, it.parentName))
        }

        return typeBuilder.build()
    }

    private fun generateMethod(packageName: String, className: String, parentName: String): MethodSpec {
        val param = ParameterSpec.builder(Vago.Customization::class.java, VAGO_CUSTOM_PARAM)
                .addAnnotation(Nullable::class.java)
                .build()
        val parcelName = "Parcel"
        val parcel = ClassName.get("android.os", parcelName)

        val beanClass = if (parentName.isEmpty()) {
            ClassName.get(packageName, className)
        } else {
            ClassName.get(packageName, parentName, className)
        }
        val vago = ClassName.get(VagoProcessor.VAGO_PACKAGE, VAGO_CLASS)
        val methodBuilder = MethodSpec
                .methodBuilder("$VAGO_TEST_METHOD_PREFIX$className$PARCEL_NAME")
                .addModifiers(PUBLIC, STATIC, FINAL)
                .addParameter(param)
                .addCode(getMethodContent(), beanClass, beanClass, vago, beanClass, parcel, parcelName, beanClass, beanClass, vago)
        return methodBuilder.build()
    }

    private fun getMethodContent(): String {
        return "\$T bean = (\$T) \$T.INSTANCE.createInstance(\$T.class, $VAGO_CUSTOM_PARAM);\n" +
                "\n" +
                "\$T parcel = \$N.obtain();\n" +
                "bean.writeToParcel(parcel, bean.describeContents());\n" +
                "parcel.setDataPosition(0);\n" +
                "\n" +
                "\$T createdFromParcel = \$T.CREATOR.createFromParcel(parcel);\n" +
                "\$T.INSTANCE.assertEquals(bean.equals(createdFromParcel), true);\n"
    }

}