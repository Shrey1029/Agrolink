// Configure the buildscript dependencies
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1") // Downgrade to a compatible AGP version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22") // Ensure Kotlin plugin version is compatible
        classpath("com.google.gms:google-services:4.3.15") // Ensure Google Services plugin is compatible
    }
}

// Configure the plugins
plugins {
    id("com.android.application") version "8.1.1" apply false // Downgrade to a compatible AGP version
    id("com.android.library") version "8.1.1" apply false // Downgrade to a compatible AGP version
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false // Ensure Kotlin plugin version is compatible
}
