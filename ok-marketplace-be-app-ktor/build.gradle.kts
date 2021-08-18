val ktorVersion: String by project
val koinVersion: String by project
//val kotlinVersion: String by project

fun DependencyHandler.ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"

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
val serializationVersion:String by project
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(ktor("server-core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktor("server-netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"
    // Gson serialization
    // implementation(ktor("gson")) // "io.ktor:ktor-ktor-gson:$ktorVersion"
    // Kotlinx serialization
    // implementation(ktor("serialization")) // "io.ktor:ktor-ktor-serialization:$ktorVersion"
    // Jackson serialization
    implementation(ktor("jackson")) // "io.ktor:ktor-ktor-jackson:$ktorVersion"

    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    // logging if you want
    implementation("ch.qos.logback:logback-classic:1.2.5")

    // transport models
    implementation(project(":ok-marketplace-be-common"))
    implementation(project(":ok-marketplace-be-transport-openapi"))
    implementation(project(":ok-marketplace-be-transport-mapping-openapi"))
    // stubs
    implementation(project(":ok-marketplace-be-stubs"))

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("server-test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
    testImplementation("io.insert-koin:koin-test:$koinVersion")
}
