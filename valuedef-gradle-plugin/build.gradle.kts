plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
    implementation(kotlin("stdlib"))
}

buildConfig {
    val compilerPluginProject = project(":valuedef-compiler-embeddable")
    packageName("${compilerPluginProject.group}.valuedef")
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${property("KOTLIN_PLUGIN_ID")}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${compilerPluginProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${compilerPluginProject.property("POM_ARTIFACT_ID")}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${compilerPluginProject.version}\"")
}

gradlePlugin {
    plugins {
        create("ValueDefGradlePlugin") {
            id = project.properties["KOTLIN_PLUGIN_ID"] as String
            displayName = "Kotlin ValueDef plugin"
            description = "Kotlin ValueDef plugin"
            implementationClass = "com.bennyhuo.kotlin.valuedef.gradle.ValueDefGradlePlugin"
        }
    }
}