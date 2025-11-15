/*
 * For more details on building Java & JVM projects, please refer to
 * https://docs.gradle.org/9.1.0/userguide/building_java_projects.html
 */

plugins {
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Jakarta Messaging 3.1
    api(libs.jakarta.jms.api)

    // Simple Logging Facade for Java (SLF4J) & Logback
    implementation(libs.bundles.sl4j.logback)
}

// Apply a specific Java toolchain to ease working on different environments.
java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

