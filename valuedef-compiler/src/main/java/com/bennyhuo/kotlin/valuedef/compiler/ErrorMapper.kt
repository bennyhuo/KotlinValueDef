package com.bennyhuo.kotlin.valuedef.compiler

/**
 * Created by benny at 2022/1/16 12:17 PM.
 */
interface ErrorMapper {
    fun <T> mapError(key: String): T
}