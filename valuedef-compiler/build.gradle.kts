import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    java
    kotlin("kapt")
    id("com.github.gmazzo.buildconfig")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    kapt("com.google.auto.service:auto-service:1.0.1")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0.1")

    testImplementation(project(":valuedef-runtime"))

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    testImplementation("com.bennyhuo.kotlin:kotlin-compile-testing-extensions:0.4")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs += listOf("-Xjvm-default=enable", "-opt-in=kotlin.RequiresOptIn")
compileKotlin.kotlinOptions.jvmTarget = "1.8"

buildConfig {
    packageName("$group.valuedef")
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${project.property("KOTLIN_PLUGIN_ID")}\"")
}