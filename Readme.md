# ValueDef

A IntelliJ plugin to provide check on 'value type' which is limited to numerical constant values. 

This is similar with the Android annotations like @IntDef and @StringDef and more. 

## Sample

```kotlin
package com.bennyhuo.kotlin.valuedef

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
```


# License

[MIT License](https://github.com/bennyhuo/KotlinValueType/blob/master/LICENSE)

    Copyright (c) 2021 Bennyhuo
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

