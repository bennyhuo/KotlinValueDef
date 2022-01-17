plugins {
    java
    id("com.github.johnrengelman.shadow")
}

jarWithEmbedded()

dependencies {
    embedded(project(":valuedef-compiler"))
}