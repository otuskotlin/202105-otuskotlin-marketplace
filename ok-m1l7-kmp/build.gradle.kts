plugins {
    kotlin("multiplatform") version "1.5.20"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */
    js(IR) {
        browser()
        nodejs()
    }
    jvm {
        withJava()
    }
    linuxX64 {
        binaries {
            executable {
                baseName = "firstKmpApp"
                debuggable
                entryPoint = "ru.otus.otuskotlin.marketplace.kmp.main"
            }
            sharedLib {
                baseName = "firstKmpLib"
            }
            staticLib {
                baseName = "firstKmpLib"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt"){
                    version {
                        strictly("1.5.0-native-mt")
                    }
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("js-big-decimal","~1.3.4"))
                implementation(npm("is-sorted","~1.0.5"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val linuxX64Main by getting {
            dependencies {

            }
        }
        val linuxX64Test by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

    }
}
