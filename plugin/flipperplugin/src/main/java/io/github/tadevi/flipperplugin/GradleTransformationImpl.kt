package io.github.tadevi.flipperplugin

import com.android.build.api.transform.*
import com.android.build.gradle.BaseExtension
import io.github.tadevi.flipperplugin.helper.isClassFile
import io.github.tadevi.flipperplugin.helper.toOutputFile
import io.github.tadevi.flipperplugin.functions.okhttp3.OkHttpClientTransformer
import org.gradle.api.Project
import java.io.File

class GradleTransformationImpl(private val project: Project) : Transform() {
    override fun getName(): String = "FlipperPluginTransformation"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.EXTERNAL_LIBRARIES,
            QualifiedContent.Scope.SUB_PROJECTS
        )
    }

    override fun isIncremental(): Boolean = true

    override fun transform(invocation: TransformInvocation) {
        val start = System.currentTimeMillis()
        try {
            if (!invocation.isIncremental) {
                invocation.outputProvider.deleteAll()
            }

            invocation.inputs.forEach { transformInput ->
                transformInput.jarInputs.forEach { jarInput ->
                    val outputJar = invocation.outputProvider.getContentLocation(
                        jarInput.name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.DIRECTORY
                    )

                    val classTransformer = createClassFileTransformer(
                        invocation.inputs,
                        invocation.referencedInputs,
                        outputJar
                    )
                    if (invocation.isIncremental) {
                        when (jarInput.status) {
                            Status.ADDED, Status.CHANGED -> classTransformer.transformJarContent(
                                jarInput.file
                            )
                            Status.REMOVED -> outputJar.delete()
                            Status.NOTCHANGED -> {
                                // nothing to do
                            }
                            else -> error("Unknown status ${jarInput.status}")
                        }
                    } else {
                        classTransformer.transformJarContent(jarInput.file)
                    }
                }

                transformInput.directoryInputs.forEach { directoryInput ->
                    val outputDir = invocation.outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY
                    )

                    val classTransformer = createClassFileTransformer(
                        invocation.inputs,
                        invocation.referencedInputs,
                        outputDir
                    )

                    if (invocation.isIncremental) {
                        directoryInput.changedFiles.forEach { (file, status) ->
                            val outputFile = toOutputFile(outputDir, directoryInput.file, file)
                            when (status) {
                                Status.ADDED, Status.CHANGED -> transformFile(
                                    file,
                                    outputFile.parentFile,
                                    classTransformer
                                )

                                Status.REMOVED -> outputFile.delete()

                                Status.NOTCHANGED -> {
                                    // nothing to do
                                }

                                else -> error("Unknown status $status")
                            }
                        }
                    } else {
                        transformDirectory(directoryInput.file, outputDir, classTransformer)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        println("Transformation time: ${System.currentTimeMillis() - start} ms")
    }

    private fun transformFile(
        inputFile: File,
        outputDir: File,
        transformer: ClassTransformer
    ) {
        if (inputFile.isClassFile()) {
            transformer.transformFile(inputFile)
        } else if (inputFile.isFile) {
            outputDir.mkdirs()
            val outputFile = File(outputDir, inputFile.name)
            inputFile.copyTo(target = outputFile, overwrite = true)
        }
    }

    private fun createClassFileTransformer(
        inputs: Collection<TransformInput>,
        referencedInputs: Collection<TransformInput>,
        outputDir: File
    ): ClassTransformer {
        val android = project.extensions.getByName("android") as BaseExtension
        val androidSdk = File(
            "${System.getenv("ANDROID_HOME")}/platforms/${android.compileSdkVersion}/android.jar"
        )
        val allInputs = (inputs + referencedInputs).flatMap { input ->
            (input.directoryInputs + input.jarInputs).map { it.file }
        }.toMutableList()
        allInputs.add(androidSdk)

        return ClassTransformer(allInputs, outputDir).apply {
            registerByteCodeTransformer(OkHttpClientTransformer())
        }
    }

    private fun transformDirectory(
        directory: File,
        outputDir: File,
        classTransformer: ClassTransformer
    ) {
        directory.walkTopDown().forEach { file ->
            val outputFile = toOutputFile(outputDir, directory, file)
            transformFile(file, outputFile, classTransformer)
        }
    }
}