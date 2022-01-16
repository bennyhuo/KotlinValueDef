// SOURCE
// FILE: Main.kt
import com.bennyhuo.kotlin.valuedef.StringDef
import com.bennyhuo.kotlin.valuedef.UnsafeDef

const val Red = "red"
const val Green = "green"
const val Yellow = "yellow"

@Target(AnnotationTarget.TYPE)
@StringDef("red", "green", "yellow")
annotation class Color

@Target(AnnotationTarget.TYPE)
@StringDef("red", "green", "yellow", "black")
annotation class ExtendedColor

fun setTrafficLightColor(color: @Color String) {
    println("current light: $color")
}

fun getColor(): @Color String = "red1"

fun getColor2(): @ExtendedColor String {
    return getColor()
}

fun strings() {
    setTrafficLightColor(color = "red") // OK
    setTrafficLightColor("black") // Error, "black" is not allowed
    setTrafficLightColor(Red) // OK
    setTrafficLightColor(Yellow) // OK
    setTrafficLightColor("r" + "ed") // OK, "red" is one of the Colors

    val color: @Color String = Red + 1
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
// GENERATED
// FILE: compiles.log
COMPILATION_ERROR
e: Main.kt: (12, 13): More than one ValueDefs are declared: [@Color, @ExtendedColor].