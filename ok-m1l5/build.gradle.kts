plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0") // http client
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3") // from string to object



    testImplementation(kotlin("test-junit"))
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
}
