plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics") // Add this line
}

android {
    namespace = "com.example.agrolink"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.agrolink"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        mlModelBinding = true // Enables ML model binding
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))

    // Firebase libraries
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-inappmessaging-display")

    // Google Play Services Auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation("androidx.activity:activity:1.9.2")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("androidx.cardview:cardview:1.0.0")

    // TensorFlow Lite dependencies
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.14.0") // Optional based on GPU usage
    implementation("org.tensorflow:tensorflow-lite-support:0.4.1") // Check for the latest version

    // Remove or comment out the litert-api dependency
    // implementation("com.google.ai.edge.litert:litert-api:1.0.1")

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
