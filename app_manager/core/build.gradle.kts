plugins {
    id("com.android.library")
    id("kotlinx-serialization")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdkVersion(AppDependency.COMPILE_SDK_VERSION)
    buildToolsVersion(AppDependency.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdkVersion(AppDependency.MIN_SDK_VERSION)
        targetSdkVersion(AppDependency.TARGET_SDK_VERSION)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf(
                    "room.exportSchema" to "false",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                ))
            }
        }
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":libtoolkit"))

    // Kotlin
    implementation(Kotlin.kotlinCore)
    implementation(Kotlin.kotlinSerialization)

    // Androidx
    implementation(AndroidX.ktx)
    implementation(AndroidX.appCompat)

    // Google
    implementation(Google.material)

    // Dagger
    implementation(Dagger.dagger)
    kapt(Dagger.daggerCompiler)

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofitRx)
    implementation(Retrofit.retrofitKotlinSerialization)
    implementation(Retrofit.retrofitXml)

    // Rx
    implementation(Rx.androidRx)
    implementation(Rx.rxRelay)

    // Room
    implementation(Room.roomRuntime)
    implementation(Room.roomKtx)
    kapt(Room.roomCompiler)

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}