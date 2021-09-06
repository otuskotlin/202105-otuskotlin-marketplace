plugins {
    kotlin("jvm")
}


dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    implementation(project(":ok-marketplace-be-service-openapi"))
    implementation(project(":ok-marketplace-be-transport-openapi"))
    implementation(project(":ok-marketplace-be-common"))
}
