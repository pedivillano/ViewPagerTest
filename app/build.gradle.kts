import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val composeVersion = rootProject.extra.get("compose_version") as String

val localPropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}
val mapsApiKey = keystoreProperties["MAPS_API_KEY"] as String

android {
    namespace = "com.petrocanada.commercial_drivers.android"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.petrocanada.commercial_drivers.android"
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = "Version 0.2 (0.2.0.0)"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["MAPS_API_KEY"] = keystoreProperties["MAPS_API_KEY"] as String
    }

    /*signingConfigs {
        getByName("debug") {
            storeFile = keystoreProperties["signing.wholesale.file"]?.let { file(it) }
            keyAlias = keystoreProperties["signing.wholesale.alias"] as String
            storePassword = keystoreProperties["signing.wholesale.password"] as String
            keyPassword = keystoreProperties["signing.wholesale.password"] as String
        }
        create("release") {
            storeFile = keystoreProperties["signing.wholesale.debug.file"]?.let { file(it) }
            keyAlias = keystoreProperties["signing.wholesale.debug.alias"] as String
            storePassword = keystoreProperties["signing.wholesale.debug.password"] as String
            keyPassword = keystoreProperties["signing.wholesale.debug.password"] as String
        }
    }*/

    buildTypes {
        getByName("release") {
            //signingConfig = signingConfigs.getByName("release")
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions ("environment")
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"

            buildConfigField("Boolean", "SHOW_VERSION_TOAST", "true")
            //signingConfig signingConfigs.debug
            manifestPlaceholders["ENABLE_CLEARTEXT_TRAFFIC"] = "false"
            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        }
        create("qut") {
            dimension = "environment"
            applicationIdSuffix = ".qut"
            buildConfigField("Boolean", "SHOW_VERSION_TOAST", "true")
            //signingConfig signingConfigs.debug
            manifestPlaceholders["ENABLE_CLEARTEXT_TRAFFIC"] = "false"
            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("Boolean", "SHOW_VERSION_TOAST", "true")
            //signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["ENABLE_CLEARTEXT_TRAFFIC"] = "false"
            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.7.0-alpha02")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // AddedF
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // SMS Auth
    implementation("com.google.android.gms:play-services-auth:17.0.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:17.4.0")

    // Video Player
    implementation("com.google.android.exoplayer:exoplayer-core:2.12.0")
    implementation("com.google.android.exoplayer:exoplayer-dash:2.12.0")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.12.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    // Retro fit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Okhttp for interceptors
    implementation("com.squareup.okhttp3:okhttp")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.0.2")

    // JWT
    api("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.11.5") {
        exclude(group = "org.json", module = "json") //provided by Android natively`
    }

    // Animated Gif
    // implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.+'

    // Edit Box with constant hint label
    //    implementation 'com.rengwuxian.materialedittext:library:2.1.4'

    // Firebase Push Notifications
    implementation(platform("com.google.firebase:firebase-bom:31.1.0"))
    implementation("com.google.firebase:firebase-messaging-ktx:22.0.0")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

    // Network error responses
    // implementation "com.github.haroldadmin:NetworkResponseAdapter:5.0.0"

    // Room database ond others
    implementation("androidx.constraintlayout:constraintlayout-core:1.0.4")
    implementation("androidx.lifecycle:lifecycle-process:2.5.1")
    implementation("androidx.room:room-common:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")


    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}

//apply plugin: 'com.google.gms.google-services'
