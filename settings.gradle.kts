rootProject.name = "marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openApiVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("org.openapi.generator") version openApiVersion
        id("com.bmuschko.docker-java-application") version bmuschkoVersion

        // spring
        val springBootVersion: String by settings
        val springDependencyVersion: String by settings
        val springPluginVersion: String by settings

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyVersion
        kotlin("plugin.spring") version springPluginVersion

        val shadowJarVersion: String by settings
        id("com.github.johnrengelman.shadow") version shadowJarVersion

    }
}

include("ok-m1l1")
include("ok-m1l3-oop")
include("ok-m1l4")
include("ok-m1l5")
include("ok-m1l6-flows-and-channels")
include("ok-m1l7-kmp")
include("ok-m2l2-testing")
include("ok-m2l4-practice")
include("ok-marketplace-be-transport-openapi")
include("ok-marketplace-mp-transport-mp")
include("ok-marketplace-be-common")
include("ok-marketplace-be-transport-mapping-kmp")
include("ok-marketplace-be-transport-mapping-openapi")
include("ok-marketplace-be-app-ktor")
include("ok-marketplace-be-app-spring")
include("ok-marketplace-be-app-serverless")
include("ok-marketplace-be-stubs")
include("ok-marketplace-be-service-openapi")
include("ok-marketplace-mp-common-cor")
include("ok-marketplace-be-logics")
include("ok-marketplace-be-common-validation")
include("ok-marketplace-be-app-rabbit")
include("ok-marketplace-be-app-kafka")
include("ok-marketplace-be-repo-test")
include("ok-marketplace-be-repo-inmemory")
include("ok-marketplace-be-repo-sql")
