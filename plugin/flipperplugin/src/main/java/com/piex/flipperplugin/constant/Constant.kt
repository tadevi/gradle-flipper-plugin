package com.piex.flipperplugin.constant

import org.objectweb.asm.Opcodes

const val ASM_API = Opcodes.ASM7
const val FLIPPER_UTIL_CLASS_DESCRIPTOR = "Lcom/piex/util/flipper/FlipperUtil;"
const val FLIPPER_UTIL_INJECT_METHOD_DESCRIPTOR = "void inject(okhttp3.OkHttpClient\$Builder)"
const val CONSTRUCTOR_METHOD_NAME = "<init>"
const val OKHTTP_CLIENT_CONSTRUCTOR_ARGS_DESCRIPTOR = "(Lokhttp3/OkHttpClient\$Builder;)V"
const val OKHTTP_CLIENT_CLASS_NAME = "okhttp3/OkHttpClient"