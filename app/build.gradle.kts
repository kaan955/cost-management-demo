plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.21"
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics") // Crashlytics Eklentisi
    id("com.google.gms.google-services")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}


android {
    namespace = "com.demo.gidermanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.demo.gidermanagement"
        minSdk = 26
        targetSdk = 34
        versionCode = 10
        versionName = "1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
     implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3-android:1.3.0")
    implementation("com.google.android.play:review-ktx:2.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.foundation:foundation-layout:1.6.8")
    implementation ("androidx.multidex:multidex:2.0.1")
    implementation ("com.jakewharton.timber:timber:4.7.1")
    implementation("io.github.dautovicharis:charts:1.3.1")
    implementation ("co.yml:ycharts:2.1.0")
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("com.google.android.material:material:1.11.0-alpha03")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

    //splash
    implementation ("androidx.core:core-splashscreen:1.0.1")


    // for navigation
    implementation("androidx.navigation:navigation-compose:2.8.0-alpha08")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("androidx.room:room-compiler:2.6.0")
    ksp("com.google.dagger:dagger-compiler:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    ksp("com.google.dagger:dagger-compiler:2.48.1")

    implementation ("org.apache.poi:poi:5.2.3")
    implementation ("org.apache.poi:poi-ooxml:5.2.3")
}