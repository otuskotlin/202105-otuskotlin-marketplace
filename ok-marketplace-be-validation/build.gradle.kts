plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":ok-marketplace-be-common"))
    implementation(project(":ok-marketplace-mp-common-cor"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
