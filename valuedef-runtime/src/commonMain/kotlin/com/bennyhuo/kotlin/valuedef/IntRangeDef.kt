package com.bennyhuo.kotlin.valuedef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class IntRangeDef(val min: Int, val max: Int)