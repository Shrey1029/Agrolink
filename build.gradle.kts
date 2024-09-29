buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2") // AGP version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22") // Kotlin plugin version
        classpath("com.google.gms:google-services:4.3.15") // Google Services plugin version
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
    }
}

plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.0" apply false // Ensure this version is compatible
}
