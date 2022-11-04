package com.piex.flipperplugin

interface ByteCodeTransformer {
    fun transform(classLoader: ClassLoader, input: ByteArray): ByteArray
}