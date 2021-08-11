plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    // transport models
    implementation(project(":ok-marketplace-be-common"))

    testImplementation(kotlin("test-junit"))
}
