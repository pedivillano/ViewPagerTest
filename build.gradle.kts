// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
}

ext {
    set("compose_version", "1.3.1")
}

tasks.register("delete", Delete::class.java) {
    delete(rootProject.buildDir)
}