import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "com.github.pnemonic.lottery"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.10")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

application {
//    mainClass.set("MainKt")
}