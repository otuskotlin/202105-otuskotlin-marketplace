package ru.otus.otuskotlin.marketplace.kmp

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformKmpTest {
    @Test
    fun allTest() {
        assertTrue("Must contain Init word") {
            PlatformKmp().init().contains("Init")
        }
    }
}
