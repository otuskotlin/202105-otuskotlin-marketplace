plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":ok-marketplace-be-common"))
    implementation(project(":ok-marketplace-be-transport-openapi"))

    testImplementation(kotlin("test"))
}
