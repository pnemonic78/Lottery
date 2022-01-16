import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "com.github.lottery"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.5.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
//    mainClass.set("MainKt")
}