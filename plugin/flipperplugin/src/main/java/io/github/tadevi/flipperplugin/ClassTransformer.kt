package io.github.tadevi.flipperplugin

import io.github.tadevi.flipperplugin.helper.copyToFolder
import io.github.tadevi.flipperplugin.helper.forEach
import io.github.tadevi.flipperplugin.helper.isClassFile
import org.objectweb.asm.ClassReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLClassLoader
import java.util.zip.ZipInputStream

class ClassTransformer(
    private val inputs: List<File>,
    private val rootOutputDir: File
) {
    private val byteCodeTransformers = mutableListOf<ByteCodeTransformer>()
    private val urlClassLoader: URLClassLoader = URLClassLoader(
        inputs.map { it.toURI().toURL() }.toTypedArray(),
        ClassLoader.getSystemClassLoader()
    )

    fun registerByteCodeTransformer(transformer: ByteCodeTransformer) {
        byteCodeTransformers.add(transformer)
    }

    fun transformFile(inputFile: File, isTransform: Boolean = true) {
        if (!isTransform) {
            inputFile.copyToFolder(rootOutputDir)
            return
        }
        inputFile.inputStream().use {
            transformClassToOutput(it)
        }
    }

    fun transformJarContent(jarInput: File, isTransform: Boolean = true) {
        ZipInputStream(FileInputStream(jarInput)).forEach { entry, inputStream ->
            if (entry.isClassFile() && isTransform) {
                transformClassToOutput(inputStream)
            } else {
                val entryFile = File(rootOutputDir, entry.name)
                if (entry.isDirectory) {
                    entryFile.mkdirs()
                } else {
                    entryFile.parentFile.mkdirs()
                    entryFile.createNewFile()
                    entryFile.writeBytes(inputStream.readBytes())
                }
            }
        }
    }

    private fun transformClassToOutput(inputStream: InputStream) {
        val byteArray = inputStream.readBytes()
        val outputFile = getOutputFile(rootOutputDir, byteArray)
        var output = byteArray

        runCatching {
            for (transformer in byteCodeTransformers) {
                output = transformer.transform(urlClassLoader, output)
            }
        }.onSuccess {
            outputFile.writeBytes(output)
        }.onFailure {
            it.printStackTrace()
            outputFile.writeBytes(byteArray)
        }
    }

    private fun getOutputFile(parent: File, classBufferArray: ByteArray): File {
        val cr = ClassReader(classBufferArray)
        val outputFile = File(parent, "${cr.className}.class")
        outputFile.parentFile.mkdirs()
        outputFile.createNewFile()
        return outputFile
    }
}