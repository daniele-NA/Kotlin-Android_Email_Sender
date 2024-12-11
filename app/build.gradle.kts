plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.view"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.view"
        minSdk = 24
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
    buildFeatures{
        viewBinding=true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //noinspection GradlePath
    implementation(files("C:/Users/cresc/Documents/DOC.DANI/Java/LIBRERIE/Email android/activation.jar"))
    //noinspection GradlePath
    implementation(files("C:/Users/cresc/Documents/DOC.DANI/Java/LIBRERIE/Email android/additionnal.jar"))
    //noinspection GradlePath
    implementation(files("C:/Users/cresc/Documents/DOC.DANI/Java/LIBRERIE/Email android/mail.jar"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.ktx) // Aggiungi questa linea
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Per la compatibilità con ViewModel
    implementation (libs.androidx.lifecycle.livedata.ktx)// Se usi LiveData

    implementation(libs.androidx.core.splashscreen.v100)





    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.material.v1120)
}