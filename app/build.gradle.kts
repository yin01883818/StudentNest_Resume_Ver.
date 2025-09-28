plugins {
    alias(libs.plugins.android.application)
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.mobileapp.studentnest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mobileapp.studentnest"
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
    // add view binding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
   // val nav_version = "2.8.9"

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.dynamic.features.fragment)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // used for Room Database
    implementation(libs.room.runtime)
    implementation(libs.firebase.firestore)
    implementation(libs.room.common.jvm)
    annotationProcessor(libs.room.compiler)
    // navigation implementations
    androidTestImplementation(libs.navigation.testing)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout.v214)
    testImplementation(libs.junit)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Used for pie chart/graphs
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("androidx.core:core-ktx:1.16.0")
}