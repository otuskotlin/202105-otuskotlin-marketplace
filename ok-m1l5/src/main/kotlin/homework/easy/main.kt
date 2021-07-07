package homework.easy

import kotlinx.coroutines.*
import java.time.Instant
import java.util.*

fun main() {

    val start = Date.from(Instant.now())
    println("start " + Date.from(Instant.now()))
    val numbers = generateNumbers()
    println("end " + Date.from(Instant.now()))
    val toFind = 10
    val toFindOther = 1000

    val foundNumbers =
        runBlocking {
            listOf(
                async { findNumberInList(toFind, numbers) },
                async { findNumberInList(toFindOther, numbers) }
            ).awaitAll()
        }

    foundNumbers.forEach {
        if (it != -1) {
            println("Your number found!")
        } else {
            println("Not found number $toFindOther")
        }
    }

    val end = Date.from(Instant.now())
    println(Date.from(Instant.now()))
    println(end.time - start.time)
}

