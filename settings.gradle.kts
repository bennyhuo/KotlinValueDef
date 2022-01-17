pluginManagement {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
        gradlePluginPortal()
    }
}

include("valuedef-runtime")
include("valuedef-inspection")
include("valuedef-compiler")
include("valuedef-compiler-embeddable")
include("valuedef-compiler-idea")
include("valuedef-gradle-plugin")
include("valuedef-common")
