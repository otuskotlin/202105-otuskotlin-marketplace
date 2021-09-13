plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.app.kafka.MainKt")
}

dependencies {
    val kafkaVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation(kotlin("test-junit"))
}
