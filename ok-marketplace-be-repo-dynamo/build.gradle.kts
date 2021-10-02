plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    val awsKotlinVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":ok-marketplace-be-common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("aws.sdk.kotlin:dynamodb:$awsKotlinVersion")
}
