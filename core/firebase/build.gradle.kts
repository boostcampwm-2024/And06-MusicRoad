import java.io.FileInputStream
import java.util.Properties

var properties = Properties()
properties.load(FileInputStream("local.properties"))

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.firebase"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            isMinifyEnabled = false

            buildConfigField(
                "String",
                "FIRESTORE_DB_ID",
                "\"${properties.getProperty("FIRESTORE_DB_ID_DEBUG")}\""
            )
        }

        release {
            isMinifyEnabled = false

            buildConfigField(
                "String",
                "FIRESTORE_DB_ID",
                "\"${properties.getProperty("FIRESTORE_DB_ID_RELEASE")}\""
            )
        }
    }
}

dependencies {
    // Firebase
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.functions.ktx)
    implementation(libs.geofire.android.common)
    implementation(libs.kotlinx.coroutines.play.services)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}
