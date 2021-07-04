package ru.otus.otuskotlin.marketplace.kmp.java

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.kmp.SuspendKmp

class SuspendJava {
    @JvmName("susp")
    fun susp() = runBlocking { SuspendKmp().susp() }

    @JvmOverloads
    fun manyDefaults(a: String = "a-val", b: String = "b-val", c: String = "c-val") {
        println("a = $a, b = $b, c = $c")
    }

    companion object {
        @JvmStatic
        fun suspMany(vararg susps: SuspendJava): Collection<String> = runBlocking {
            susps
                .asFlow()
                .map { async { it.susp() } }
                .toList()
                .awaitAll()
        }
    }
}

// Requires JDK 15
//@JvmRecord
data class X(
    @JvmField
    val x: String = ""
)
