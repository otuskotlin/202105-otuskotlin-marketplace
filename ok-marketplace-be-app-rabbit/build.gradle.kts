plugins {
    kotlin("jvm")
}


dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(":ok-marketplace-be-service-openapi"))
    implementation(project(":ok-marketplace-be-transport-openapi"))
    implementation(project(":ok-marketplace-be-transport-mapping-openapi"))
    implementation(project(":ok-marketplace-be-common"))
}
