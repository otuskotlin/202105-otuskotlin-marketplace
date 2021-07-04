package ru.otus.otuskotlin.marketplace.kmp

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformKmpLinuxX64Test {
    @Test
    fun allTest() {
        assertTrue("Must contain Platform word") {
            PlatformKmp().init().contains("LinuxX64")
        }
    }
}

