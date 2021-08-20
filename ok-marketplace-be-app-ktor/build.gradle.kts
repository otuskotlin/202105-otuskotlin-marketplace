val ktorVersion: String by project
//val kotlinVersion: String by project

fun DependencyHandler.ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("(c) Otus")
        ports.set(listOf(8080))
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest"
            )
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

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
}
