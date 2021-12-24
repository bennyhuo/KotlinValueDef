package com.bennyhuo.kotlin.valuedef

/**
 * Created by benny.
 */
fun setTrafficLightColor(color: @Color String) {
    println("current light: $color")
}

fun getColor(): @Color String = "red1"

fun getColor2(): @MoreColor String {
    return getColor()
}

fun strings() {
    setTrafficLightColor(color = "red") // OK
    setTrafficLightColor("black") // Error, "black" is not allowed
    setTrafficLightColor(Red) // OK
    setTrafficLightColor(Yellow) // OK
    setTrafficLightColor("r" + "ed") // OK, "red" is one of the Colors

    val color: @Color @MoreColor String = Red + 1
    var color2: @Color String = @UnsafeDef ""
    color2 = getColor() + "1"
    setTrafficLightColor(color) // OK, color is type of @Color

    val unsafeColor: String = getColor()
    setTrafficLightColor(@UnsafeDef unsafeColor ) // OK, unsafe but on your own
    setTrafficLightColor(unsafeColor) // Error, unknown String value is not allowed

    setTrafficLightColor(getColor()) //
    setTrafficLightColor("red") // KtLiteralStringTemplateEntry
    setTrafficLightColor("${getColor()}") // KtBlockStringTemplateEntry
    setTrafficLightColor("re\$") // KtEscapeStringTemplateEntry
    setTrafficLightColor("$Red") // KtSimpleNameStringTemplateEntry
    setTrafficLightColor(getColor())
}

fun setState(state: @State Int) {

}

fun ints() {
    val state: Int = 10
    val state2: @State Int = 4 - 1
    setState(state)
    setState(@UnsafeDef state)
    setState(state2)

    val state3: @State2 Int = 11
}