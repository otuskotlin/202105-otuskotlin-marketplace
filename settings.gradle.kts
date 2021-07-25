rootProject.name = "marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openApiVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion

        id("org.openapi.generator") version openApiVersion
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
