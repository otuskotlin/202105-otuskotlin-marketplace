plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":ok-marketplace-be-common"))
    implementation(project(":ok-marketplace-mp-common-cor"))
    implementation(project(":ok-marketplace-be-stubs"))
    implementation(project(":ok-marketplace-be-common-validation"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}
