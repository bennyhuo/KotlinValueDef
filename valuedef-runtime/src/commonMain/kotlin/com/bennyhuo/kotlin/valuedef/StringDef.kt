package com.bennyhuo.kotlin.valuedef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class StringDef(vararg val value: String)