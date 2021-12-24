package com.bennyhuo.kotlin.valuedef

const val Red = "red"
const val Green = "green"
const val Yellow = "yellow"

@Target(AnnotationTarget.CLASS)
annotation class StringDef(vararg val value: String)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.EXPRESSION)
annotation class UnsafeDef

@Target(AnnotationTarget.TYPE)
@StringDef(Red, Yellow, Green)
annotation class Color

@Target(AnnotationTarget.TYPE)
@StringDef(Red, Yellow, Green, "black")
annotation class MoreColor

// ------------------

@Target(AnnotationTarget.CLASS)
annotation class IntDef(vararg val value: Int)

@Target(AnnotationTarget.CLASS)
annotation class IntRangeDef(val min: Int, val max: Int)

@Target(AnnotationTarget.TYPE)
@IntDef(1, 2, 3)
@StringDef("hello")
annotation class State

@Target(AnnotationTarget.TYPE)
@IntRangeDef(1, 10)
annotation class State2