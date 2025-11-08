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

}

// Apply a specific Java toolchain to ease working on different environments.
java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

