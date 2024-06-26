plugins {
    application
    kotlin("jvm") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
}

group = "io.cursedfunction"
version = "1.0-SNAPSHOT"

application {
    mainClass = "io.cursedfunction.app.MainKt"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(project(":annotation"))
    ksp(project(":annotation"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
