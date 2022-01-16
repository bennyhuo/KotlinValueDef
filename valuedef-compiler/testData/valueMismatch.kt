// SOURCE
// FILE: Main.kt
import com.bennyhuo.kotlin.valuedef.StringDef
import com.bennyhuo.kotlin.valuedef.UnsafeDef

@Target(AnnotationTarget.TYPE)
@StringDef("red", "green", "yellow")
annotation class Color

@Target(AnnotationTarget.TYPE)
@StringDef("red", "green", "yellow", "black")
annotation class ExtendedColor

val color: @Color String = "black"
val color2: @ExtendedColor String = "black"

val color3: @Color String = color2

fun strings() {
    val localColor: @Color String = "blue"
    parameters(localColor)
    parameters(@UnsafeDef returnTypes())
}

fun parameters(color: @ExtendedColor String) {

}

fun returnTypes(): String = TODO()
// GENERATED
// FILE: compiles.log
COMPILATION_ERROR
e: Main.kt: (12, 28): Value mismatch. Must be in [red, green, yellow].
e: Main.kt: (15, 29): Value mismatch. Must be in [red, green, yellow].
e: Main.kt: (18, 37): Value mismatch. Must be in [red, green, yellow].
e: Main.kt: (19, 16): Value mismatch. Must be in [red, green, yellow, black].