plugins {
    kotlin("jvm")
}

dependencies {

    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":ok-marketplace-be-common"))

    api(kotlin("test-junit5"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}
