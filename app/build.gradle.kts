import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").reader())

fun getLocalProperty(key: String): String {
    return properties.getProperty(key) ?: ""
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20" apply false
//    id(libs.plugins.kotlin.serialization)
//    id("kotlin-kapt")
    id("com.google.devtools.ksp") version ("2.2.20-2.0.3")
//    id(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.numtory.application"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.numtory"
        minSdk = 23
        targetSdk = 36
        versionCode = 3
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a", "x86_64"))
        }
        buildConfigField("String", "BITPIN_URL", getLocalProperty("BITPIN_URL"))
        buildConfigField("String", "NUBITEX_URL", getLocalProperty("NUBITEX_URL"))
        buildConfigField("String", "NUBITEX_MARKET_URL", getLocalProperty("NUBITEX_MARKET_URL"))
        buildConfigField("String", "BIT24_URL", getLocalProperty("BIT24_URL"))
        buildConfigField("String", "BIT24_MARKET_URL", getLocalProperty("BIT24_MARKET_URL"))
        buildConfigField("String", "ABANTEHTER_URL", getLocalProperty("ABANTEHTER_URL"))
        buildConfigField("String", "PINGI_URL", getLocalProperty("PINGI_URL"))
        buildConfigField("String", "TABDEAL_URL", getLocalProperty("TABDEAL_URL"))
        buildConfigField("String", "TABDEAL_MARKET_URL", getLocalProperty("TABDEAL_MARKET_URL"))
        buildConfigField("String", "UBITEX_URL", getLocalProperty("UBITEX_URL"))
        buildConfigField("String", "ETEREX_URL", getLocalProperty("ETEREX_URL"))
        buildConfigField("String", "TETHERLAND_URL", getLocalProperty("TETHERLAND_URL"))
        buildConfigField("String", "ARZPLUS_URL", getLocalProperty("ARZPLUS_URL"))
        buildConfigField("String", "ARZPLUS_MARKET_URL", getLocalProperty("ARZPLUS_MARKET_URL"))
        buildConfigField("String", "SARMAYEX_URL", getLocalProperty("SARMAYEX_URL"))
        buildConfigField("String", "SARMAYEX_MARKET_URL", getLocalProperty("SARMAYEX_MARKET_URL"))
        buildConfigField("String", "POOLENO_URL", getLocalProperty("POOLENO_URL"))
        buildConfigField("String", "TWOX_URL", getLocalProperty("TWOX_URL"))
        buildConfigField("String", "NUMTORY_URL", getLocalProperty("NUMTORY_URL"))
        buildConfigField("String", "WALLEX_URL", getLocalProperty("WALLEX_URL"))
        buildConfigField("String", "SARAF_URL", getLocalProperty("SARAF_URL"))
        buildConfigField("String", "COINKADE_URL", getLocalProperty("COINKADE_URL"))
        buildConfigField("String", "RAMZINEX_URL", getLocalProperty("RAMZINEX_URL"))
        buildConfigField("String", "ADTRACE_TOKEN", getLocalProperty("ADTRACE_TOKEN"))

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.gson)

    implementation(libs.kotlinx.coroutines.core)

    implementation (libs.compose.destinations)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    ksp (libs.compose.destinations.ksp)
    ksp (libs.androidx.room.compiler)

    // Koin for Android
    implementation(libs.koin.android)
    // Koin for Jetpack Compose
    implementation(libs.koin.androidx.compose)

    implementation(libs.slf4j.simple)

    implementation(libs.android.sdk)
    implementation(libs.installreferrer)
    implementation(libs.play.services.ads.identifier)
}