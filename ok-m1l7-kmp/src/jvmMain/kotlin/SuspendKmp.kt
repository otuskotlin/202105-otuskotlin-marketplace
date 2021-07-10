package ru.otus.otuskotlin.marketplace.kmp

import kotlinx.coroutines.delay
import java.time.Instant

actual class SuspendKmp {
    actual suspend fun susp(): String {
        println("Start at ${Instant.now().toString()}")
        delay(1000)
        println("Stop at ${Instant.now().toString()}")
        return "Suspend JVM"
    }
}
