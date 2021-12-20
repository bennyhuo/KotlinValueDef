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

@Target(AnnotationTarget.TYPE)
@ValueType(Red, Yellow, Green, "black")
annotation class Color2

fun setTrafficLightColor(color: @Color String) {
    println("current light: $color")
}

fun getColor(): @Color String = "red1" // Error, Wrong value. Only one of [red, yellow, green] is allowed. 

fun getColor2(): @Color2 String {
    return getColor() // OK, @Color is a subset of @Color2
}

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
    setTrafficLightColor(getColor()) // OK, getColor returns '@Color String' 

    val color2: @Color @Color2 String = Red + 1 // Error, More than one value types are declared: [@Color, @Color2]. 
    val color3: @Color Int = 1 // Error, Value type 'kotlin.String' is not compatible with declared type 'kotlin.Int'. 
    

}