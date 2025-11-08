/*
 * For more details on building Java & JVM projects, please refer to
 * https://docs.gradle.org/9.1.0/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Jakarta Messaging 3.1
    implementation(libs.jakarta.jms.api)
    runtimeOnly(libs.activemq.jms.imp)

    // Simple Logging Facade for Java (SLF4J) & Logback
    implementation(libs.bundles.sl4j.logback)

    // JCommander
    implementation(libs.jcommander)

    // Utilidades
    implementation(project(":UtilGeneral"))
    implementation(project(":UtilJMS"))
}

// Apply a specific Java toolchain to ease working on different environments.
java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

application {
    // Define the main class for the application.
    mainClass = "cl.hcs.jms.EnviarConJNDI"
}
