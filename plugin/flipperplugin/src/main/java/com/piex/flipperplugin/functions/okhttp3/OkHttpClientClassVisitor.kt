package com.piex.flipperplugin.functions.okhttp3

import com.piex.flipperplugin.constant.ASM_API
import com.piex.flipperplugin.constant.CONSTRUCTOR_METHOD_NAME
import com.piex.flipperplugin.constant.OKHTTP_CLIENT_CONSTRUCTOR_ARGS_DESCRIPTOR
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class OkHttpClientClassVisitor(cv: ClassVisitor) : ClassVisitor(ASM_API, cv) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == CONSTRUCTOR_METHOD_NAME && descriptor == OKHTTP_CLIENT_CONSTRUCTOR_ARGS_DESCRIPTOR) {
            return OkHttpClientBuilderAdviceAdapter(mv, name, access, descriptor)
        }
        return mv
    }
}