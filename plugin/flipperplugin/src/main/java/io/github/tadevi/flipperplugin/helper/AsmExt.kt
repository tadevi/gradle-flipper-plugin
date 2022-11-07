package io.github.tadevi.flipperplugin.helper

import org.objectweb.asm.Type
import org.objectweb.asm.commons.GeneratorAdapter
import org.objectweb.asm.commons.Method


fun type(descriptor: String): Type = Type.getType(descriptor)

fun method(descriptor: String): Method = Method.getMethod(descriptor)

val String.type: Type
    get() = Type.getType(this)

val String.method: Method
    get() = Method.getMethod(this)

fun GeneratorAdapter.invokeVirtual(owner: String, method: String) {
    invokeVirtual(type(owner), method(method))
}

fun GeneratorAdapter.invokeStatic(owner: String, method: String) {
    invokeStatic(type(owner), method(method))
}