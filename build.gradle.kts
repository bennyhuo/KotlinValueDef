plugins {
    java
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.intellij") version "1.3.0"
}

group = "com.bennyhuo.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.2.3")
    plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
}
tasks {
    patchPluginXml {
        changeNotes.set("""
            Add change notes here.<br>
            <em>most HTML tags may be used</em>        """.trimIndent())
    }
    
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}