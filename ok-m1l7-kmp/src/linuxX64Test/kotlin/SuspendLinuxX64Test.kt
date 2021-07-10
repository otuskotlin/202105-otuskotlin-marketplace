package ru.otus.otuskotlin.marketplace.kmp

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SuspendLinuxX64Test {
    @Test
    fun suspendTest(): Unit = runBlocking {
        assertEquals("Suspend LinuxX64", SuspendKmp().susp())
    }
}
