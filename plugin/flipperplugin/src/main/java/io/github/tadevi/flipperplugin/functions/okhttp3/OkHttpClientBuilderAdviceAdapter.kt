package io.github.tadevi.flipperplugin.functions.okhttp3

import io.github.tadevi.flipperplugin.constant.ASM_API
import io.github.tadevi.flipperplugin.constant.FLIPPER_UTIL_CLASS_DESCRIPTOR
import io.github.tadevi.flipperplugin.constant.FLIPPER_UTIL_INJECT_METHOD_DESCRIPTOR
import io.github.tadevi.flipperplugin.helper.invokeStatic
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