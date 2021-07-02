package continuation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    coroutine()
}

suspend fun coroutine() {
    println("Coroutine starts")
    delay(1000L)
    println("Coroutine middle stage")
    delay(2000L)
    println("Coroutine has ended")
}

fun CoroutineScope.coroutineAsync() = async {
    println("Work started")
}