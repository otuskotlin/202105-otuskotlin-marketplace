val ktor: String by project

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    // ktor dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("io.ktor:ktor-server-core:$ktor")
    implementation("io.ktor:ktor-server-netty:$ktor")

    // logging
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // transport models
    implementation(project(":ok-marketplace-be-common"))
    implementation(project(":ok-marketplace-be-transport-openapi"))
    implementation(project(":ok-marketplace-be-transport-mapping-openapi"))
    // stubs
    implementation(project(":ok-marketplace-be-stubs"))
}