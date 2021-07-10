package ru.otus.otuskotlin.marketplace.kmp

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SuspendJvmTest {
    @Test
    fun suspendTest(): Unit = runBlocking {
        assertEquals("Suspend JVM", SuspendKmp().susp())
    }
}
