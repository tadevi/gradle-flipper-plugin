package com.piex.flipperplugin.functions.okhttp3

import com.piex.flipperplugin.ByteCodeTransformer
import com.piex.flipperplugin.constant.OKHTTP_CLIENT_CLASS_NAME
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class OkHttpClientTransformer : ByteCodeTransformer {
    override fun transform(classLoader: ClassLoader, input: ByteArray): ByteArray {
        val cr = ClassReader(input)

        if (cr.className == OKHTTP_CLIENT_CLASS_NAME) {
            val cw = object : ClassWriter(cr, COMPUTE_FRAMES) {
                override fun getClassLoader() = classLoader
            }
            val cv = OkHttpClientClassVisitor(cw)
            cr.accept(cv, ClassReader.EXPAND_FRAMES)
            return cw.toByteArray()
        }

        return input
    }
}