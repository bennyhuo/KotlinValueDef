// SOURCE
// FILE: Main.kt
import com.bennyhuo.kotlin.valuedef.IntDef
import com.bennyhuo.kotlin.valuedef.IntRangeDef
import com.bennyhuo.kotlin.valuedef.UnsafeDef

@Target(AnnotationTarget.TYPE)
@IntDef(1, 2, 3)
@StringDef("hello")
annotation class State

@Target(AnnotationTarget.TYPE)
@IntRangeDef(1, 10)
annotation class State2

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
// GENERATED
// FILE: compiles.log
COMPILATION_ERROR
e: Main.kt: (12, 13): More than one ValueDefs are declared: [@Color, @ExtendedColor].