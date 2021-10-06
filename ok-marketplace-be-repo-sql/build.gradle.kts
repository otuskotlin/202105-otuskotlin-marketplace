plugins {
    kotlin("jvm")
}

dependencies {
    val hikariVersion: String by project
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":ok-marketplace-be-common"))

    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation(project(":ok-marketplace-be-repo-test"))
}