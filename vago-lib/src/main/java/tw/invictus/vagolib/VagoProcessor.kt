package tw.invictus.vagolib

import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import tw.invictus.api.VagoMethod
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * Created by ivan on 2018/6/19.
 */
@AutoService(Processor::class)
class VagoProcessor : AbstractProcessor() {

    private var log: Messager? = null

    @Synchronized
    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        log = p0?.messager
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(VagoMethod::class.java.canonicalName)
    }

    @Suppress("MISSING_DEPENDENCY_CLASS")
    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        p1?.getElementsAnnotatedWith(VagoMethod::class.java)?.forEach {
            val exElement = it as ExecutableElement
            val clazzName = exElement.enclosingElement.simpleName.toString()
            val methodName = exElement.simpleName.toString()
            val returnClassPath = exElement.returnType.toString()
            val packageName = processingEnv.elementUtils.getPackageOf(it).qualifiedName.toString()

            val generator = MethodTestGenerator(clazzName, packageName, listOf(MethodTestContent(methodName, returnClassPath)))
            val generatedClass = generator.generateTestClass()
            val file = JavaFile.builder(packageName, generatedClass).build()
            val filer = processingEnv.filer
            log?.printMessage(Diagnostic.Kind.NOTE, "Tests for $clazzName were generated.")
            file.writeTo(filer)
        }

        return true
    }
}