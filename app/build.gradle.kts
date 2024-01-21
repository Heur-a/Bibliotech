plugins {
    id("com.android.application")
    // FIREBASE
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bibliotech"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.bibliotech"
        minSdk = 26
        targetSdk = 33
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0");
    implementation("com.google.firebase:firebase-firestore:24.9.1")

    // QR utility implementation
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    // Graph implement
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.airbnb.android:lottie:4.2.0") // libreria para la animacion de carga
    implementation ("com.google.android.material:material:1.0.0") // libreria para el diseño de la app
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.ramijemli.percentagechartview:percentagechartview:0.3.1")
    implementation ("com.airbnb.android:lottie:4.2.0") // libreria para la animacion de carga
    implementation ("com.google.android.material:material:1.0.0") // libreria p
    //implementacion img zoom
    implementation("com.github.MikeOrtiz:TouchImageView:1.4.1")
    implementation ("com.firebaseui:firebase-ui-storage:7.2.0")
    implementation ("com.google.android.gms:play-services-tasks:17.2.0")
    implementation ("com.google.firebase:firebase-appcheck:16.0.0-beta02")
    implementation("com.google.firebase:firebase-analytics")
    implementation ("org.apache.commons:commons-math3:3.6.1")
    implementation("org.ejml:ejml-all:0.39")




}
