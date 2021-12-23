package com.bennyhuo.kotlin.valuedef

import okhttp3.internal.toHexString

/**
 * Created by benny.
 */
inline fun Any.log(message: Any?) {
    println("${Throwable().stackTrace.firstOrNull()?.methodName}: [${Thread.currentThread().name} ${this.hashCode().toHexString()}] $message")
}

inline fun <R> Any.logTime(text: String, block: () -> R): R {
    val start = System.currentTimeMillis()
    val result = block()
    log("$text - ${System.currentTimeMillis() - start}")
    return result
}