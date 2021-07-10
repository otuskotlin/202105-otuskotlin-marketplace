package ru.otus.otuskotlin.marketplace.kmp

import kotlin.test.Test
import kotlin.test.assertEquals

class DataClassTest {
    @Test
    fun dataClassTest() {
        assertEquals("some", DataClass(some = "some").some)
    }
}
