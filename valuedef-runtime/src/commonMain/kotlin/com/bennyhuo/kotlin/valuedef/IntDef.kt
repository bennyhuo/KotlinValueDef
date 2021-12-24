package com.bennyhuo.kotlin.valuedef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class IntDef(vararg val value: Int)