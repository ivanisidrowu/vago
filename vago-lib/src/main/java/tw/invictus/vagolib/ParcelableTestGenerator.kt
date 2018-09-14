package tw.invictus.vagolib

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import org.jetbrains.annotations.Nullable
import tw.invictus.vagolib.VagoProcessor.Companion.VAGO_CLASS
import tw.invictus.vagolib.VagoProcessor.Companion.VAGO_CUSTOM_PARAM
import javax.lang.model.element.Modifier.*

class ParcelableTestGenerator(
        private val className: String,
        private val packageName: String
): VagoGenerator {

    private val parcelableName = "Parcelable"

    override fun generate(): TypeSpec {
        val typeBuilder = TypeSpec
                .classBuilder(VAGO_CLASS + className + parcelableName)
                .addModifiers(PUBLIC, FINAL)
        typeBuilder.addMethod(generateMethod())

        return typeBuilder.build()
    }

    private fun generateMethod(): MethodSpec {
        val param = ParameterSpec.builder(Vago.Customization::class.java, VAGO_CUSTOM_PARAM)
                .addAnnotation(Nullable::class.java)
                .build()
        val parcelName = "Parcel"
        val parcel = ClassName.get("android.os", parcelName)
        val beanClass = ClassName.get(packageName, className)
        val vago = ClassName.get(VagoProcessor.VAGO_PACKAGE, VAGO_CLASS)
        val methodBuilder = MethodSpec
                .methodBuilder(VagoProcessor.VAGO_TEST_METHOD_PREFIX + parcelableName)
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