package com.bennyhuo.kotlin.valuedef.common.utils

/**
 * Created by benny at 2022/1/17 10:36 AM.
 */
inline fun <reified T> Any?.safeAs() = this as? T
inline fun <reified T> Any.caseAs() = this as T