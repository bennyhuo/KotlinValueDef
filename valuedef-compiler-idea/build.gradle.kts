plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij") version("1.1.2")
}

dependencies {
    implementation(project(":valuedef-compiler"))
}

intellij {
    version.set("2021.3.1")
    plugins.set(listOf("Kotlin", "com.intellij.gradle"))
    updateSinceUntilBuild.set(false)
//    alternativeIdePath props["AndroidStudio.path"]
//    alternativeIdePath props["intellijIU.path"]

}