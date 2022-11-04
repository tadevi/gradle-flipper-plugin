package com.piex.flipperplugin.functions.okhttp3

import com.piex.flipperplugin.constant.ASM_API
import com.piex.flipperplugin.constant.FLIPPER_UTIL_CLASS_DESCRIPTOR
import com.piex.flipperplugin.constant.FLIPPER_UTIL_INJECT_METHOD_DESCRIPTOR
import com.piex.flipperplugin.helper.invokeStatic
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class OkHttpClientBuilderAdviceAdapter(
    mv: MethodVisitor, name: String?, access: Int, descriptor: String?
) : AdviceAdapter(ASM_API, mv, access, name, descriptor) {

    override fun onMethodEnter() {
        loadArg(0)
        invokeStatic(
            owner = FLIPPER_UTIL_CLASS_DESCRIPTOR,
            method = FLIPPER_UTIL_INJECT_METHOD_DESCRIPTOR
        )
    }

    override fun onMethodExit(opcode: Int) {
    }
}