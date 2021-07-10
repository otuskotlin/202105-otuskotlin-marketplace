package ru.otus.otuskotlin.marketplace.kmp

import kotlinx.coroutines.delay

actual class SuspendKmp {
    actual suspend fun susp(): String {
        delay(1000)
        return "Suspend JS"
    }
}
