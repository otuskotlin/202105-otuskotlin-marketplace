package ru.otus.otuskotlin.marketplace.kmp

import kotlinx.coroutines.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SuspendJsTest {
    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun suspendTest() = MainScope().promise {
        assertEquals("Suspend JS", SuspendKmp().susp())
    }
}
