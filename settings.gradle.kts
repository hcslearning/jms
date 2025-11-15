/*
 * For more detailed information on multi-project builds, please refer to Gradle documentation:
 * https://docs.gradle.org/9.1.0/userguide/multi_project_builds.html
 * https://docs.gradle.org/current/userguide/best_practices_structuring_builds.html
 */

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "JakartaMessaging"

// ################ ENVIO #####################
include(":EnviarConJNDI")
project(":EnviarConJNDI").projectDir = file("Enviar/ConJNDI")

include(":EnviarSinJNDI")
project(":EnviarSinJNDI").projectDir = file("Enviar/SinJNDI")


// ################ RECEPCION COLA #####################
include(":RecibirDesdeCola")
project(":RecibirDesdeCola").projectDir = file("Recibir/Cola")


// ################ RECEPCION TOPICO #####################
include(":SuscripcionNoDurableNoCompartida")
project(":SuscripcionNoDurableNoCompartida").projectDir = file("Recibir/Topico/NoDurableNoCompartida")

include(":SuscripcionNoDurableCompartida")
project(":SuscripcionNoDurableCompartida").projectDir = file("Recibir/Topico/NoDurableCompartida")


// ################ UTIL #####################
include(":UtilGeneral")
project(":UtilGeneral").projectDir = file("Util/General")

include(":UtilJMS")
project(":UtilJMS").projectDir = file("Util/JMS")