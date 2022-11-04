package com.piex.flipperplugin.helper

import com.android.SdkConstants
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun copyJar(inputJar: File, outputJar: File) {
    outputJar.parentFile?.mkdirs()
    inputJar.copyTo(target = outputJar, overwrite = true)
}

fun toOutputFile(outputDir: File, inputDir: File, inputFile: File): File {
    return File(outputDir, inputFile.relativeTo(inputDir).path)
}

fun File.copyToFolder(folder: File) {
    val newFile = File(folder, relativeTo(folder).path)
    copyTo(newFile, overwrite = true)
}

fun ZipEntry.isClassFile(): Boolean {
    return !this.isDirectory && this.name.endsWith(SdkConstants.DOT_CLASS)
}

fun File.isClassFile(): Boolean {
    return !isDirectory && extension == SdkConstants.EXT_CLASS
}

fun ZipInputStream.forEach(block: (ZipEntry, ZipInputStream) -> Unit) {
    use {
        var entry = nextEntry
        while (entry != null) {
            block(entry, this)
            entry = nextEntry
        }
    }
}