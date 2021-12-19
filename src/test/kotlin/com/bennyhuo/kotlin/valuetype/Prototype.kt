package com.bennyhuo.kotlin.valuetype

/**
 * Created by benny.
 */
const val Red = "red"
const val Green = "green"
const val Yellow = "yellow"

@Target(AnnotationTarget.CLASS)
annotation class ValueType(vararg val value: String)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.EXPRESSION)
annotation class UnsafeValueType

@Target(AnnotationTarget.TYPE)
@ValueType(Red, Yellow, Green)
annotation class Color

fun setTrafficLightColor(color: @Color String) {
    println("current light: $color")
}

fun getColor(): String = TODO()

fun main() {
    setTrafficLightColor("red") // OK
    setTrafficLightColor("black") // Error, "black" is not allowed
    setTrafficLightColor(Red) // OK
    setTrafficLightColor(Yellow) // OK
    setTrafficLightColor("r" + "ed") // OK, "red" is one of the Colors
    
    val color: @Color String = Red
    setTrafficLightColor(color) // OK, color is type of @Color

    val unsafeColor: String = getColor()
    setTrafficLightColor(@UnsafeValueType unsafeColor) // OK, unsafe but on your own
    setTrafficLightColor(unsafeColor) // Error, unknown String value is not allowed
}