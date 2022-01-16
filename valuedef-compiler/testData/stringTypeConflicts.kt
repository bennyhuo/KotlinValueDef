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

val color: @Color @ExtendedColor String = "red"

fun strings() {
    val localColor: @Color @ExtendedColor String = "blue"
}

fun parameters(color: @Color @ExtendedColor String) {

}

fun returnTypes(): @Color @ExtendedColor String {
    TODO()
}
// GENERATED
// FILE: compiles.log
COMPILATION_ERROR
e: Main.kt: (16, 12): More than one ValueDefs are declared: [@Color, @ExtendedColor].
e: Main.kt: (19, 21): More than one ValueDefs are declared: [@Color, @ExtendedColor].
e: Main.kt: (22, 23): More than one ValueDefs are declared: [@Color, @ExtendedColor].
e: Main.kt: (26, 20): More than one ValueDefs are declared: [@Color, @ExtendedColor].