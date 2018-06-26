package tw.invictus.vagolib

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import org.jetbrains.annotations.Nullable
import javax.lang.model.element.Modifier.*

/**
 * Created by ivan on 2018/6/22.
 */
class MethodTestGenerator(
        private val className: String,
        private val packageName: String,
        private val contents: List<MethodTestContent>
) {
    private val vagoPackage = "tw.invictus.vagolib"
    private val vagoClassName = "Vago"
    private val methodNamePrefix = "test"
    private val methodParamName = "customization"

    fun generateTestClass(): TypeSpec {
        val typeBuilder = TypeSpec
                .classBuilder(vagoClassName + className)
                .addModifiers(PUBLIC, FINAL)

        contents.forEach {
            typeBuilder.addMethod(generateTestMethod(it))
        }

        return typeBuilder.build()
    }

    private fun generateTestMethod(content: MethodTestContent): MethodSpec {
        val vago = ClassName.get(vagoPackage, vagoClassName)
        val param = ParameterSpec.builder(Vago.Customization::class.java, methodParamName)
                .addAnnotation(Nullable::class.java)
                .build()
        val methodBuilder = MethodSpec
                .methodBuilder(methodNamePrefix + content.methodName.capitalize())
                .addModifiers(PUBLIC, STATIC, FINAL)
                .addParameter(param)
                .addStatement(getTestMethodString(content), vago)
        return methodBuilder.build()
    }

    private fun getTestMethodString(content: MethodTestContent): String {
        return "\$T.INSTANCE.testTransformation(\"$className\", \"${content.methodName}\", \"${content.returnClassPath}\", \"$packageName\", $methodParamName)"
    }
}