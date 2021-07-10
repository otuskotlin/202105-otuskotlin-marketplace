package ru.otus.otuskotlin.marketplace.kmp

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformKmpJvmTest {
    @Test
    fun allTest() {
        assertTrue("Must contain Platform word") {
            PlatformKmp().init().contains("JVM")
        }
    }
}
