package tw.invictus.vagolib

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import org.jetbrains.annotations.Nullable
import tw.invictus.vagolib.VagoProcessor.Companion.VAGO_CLASS
import tw.invictus.vagolib.VagoProcessor.Companion.VAGO_CUSTOM_PARAM
import tw.invictus.vagolib.VagoProcessor.Companion.VAGO_PACKAGE
import tw.invictus.vagolib.VagoProcessor.Companion.VAGO_TEST_METHOD_PREFIX
import javax.lang.model.element.Modifier.*

/**
 * Created by ivan on 2018/6/22.
 */
class MethodTestGenerator(
        private val className: String,
        private val packageName: String,
        private val contents: List<MethodTestContent>
): VagoGenerator {

    override fun generate(): TypeSpec {
        val typeBuilder = TypeSpec
                .classBuilder(VAGO_CLASS + className)
                .addModifiers(PUBLIC, FINAL)

        contents.forEach {
            typeBuilder.addMethod(generateTestMethod(it))
        }

        return typeBuilder.build()
    }

    private fun generateTestMethod(content: MethodTestContent): MethodSpec {
        val vago = ClassName.get(VAGO_PACKAGE, VAGO_CLASS)
        val param = ParameterSpec.builder(Vago.Customization::class.java, VAGO_CUSTOM_PARAM)
                .addAnnotation(Nullable::class.java)
                .build()
        val methodBuilder = MethodSpec
                .methodBuilder(VAGO_TEST_METHOD_PREFIX + content.methodName.capitalize())
                .addModifiers(PUBLIC, STATIC, FINAL)
                .addParameter(param)
                .addStatement(getTestMethodString(content), vago)
        return methodBuilder.build()
    }

    private fun getTestMethodString(content: MethodTestContent): String {
        return "\$T.INSTANCE.testTransformation(\"$className\", \"${content.methodName}\", \"${content.returnClassPath}\", \"$packageName\", $VAGO_CUSTOM_PARAM)"
    }
}