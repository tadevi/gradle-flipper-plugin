package io.github.tadevi.flipperplugin

interface ByteCodeTransformer {
    fun transform(classLoader: ClassLoader, input: ByteArray): ByteArray
}