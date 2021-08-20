import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}
val kotestVersion = "4.6.1"
group = rootProject.group
version = rootProject.version
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator") // info; refresh; springMvc output
    implementation("org.springframework.boot:spring-boot-starter-web") // Controller, Service, etc..
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // from models to json and Vice versa
    implementation("org.jetbrains.kotlin:kotlin-reflect") // for spring-boot app
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // for spring-boot app
    developmentOnly("org.springframework.boot:spring-boot-devtools") // for hot load classes

    // transport models
    implementation(project(":ok-marketplace-be-common"))
    implementation(project(":ok-marketplace-be-transport-openapi"))
    implementation(project(":ok-marketplace-be-transport-mapping-openapi"))

    implementation(project(":ok-marketplace-be-stubs"))

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("com.ninja-squad:springmockk:3.0.1") // mockking beans
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    bootBuildImage {
        imageName = "${project.name}:${project.version}"
    }
}
