package com.bennyhuo.kotlin.valuedef.compiler

/**
 * Created by benny.
 */
inline fun Any.log(message: Any?) {
    println("${Throwable().stackTrace.firstOrNull()?.methodName}: [${Thread.currentThread().name} ${this.hashCode().toString(16)}] $message")
}

inline fun <R> Any.logTime(text: String, block: () -> R): R {
    val start = System.currentTimeMillis()
    val result = block()
    log("$text - ${System.currentTimeMillis() - start}")
    return result
}