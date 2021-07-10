package coroutinescope

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

fun CoroutineScope.suspendingCall(ctx: CoroutineContext) =
    launch(ctx) {
        coroutineScope {
            delay(500)
            println("foo bar")
        }
    }

fun CoroutineScope.blockingCall(ctx: CoroutineContext) =
    launch(ctx) {
        runBlocking {
            delay(500)
            println("foo bar")
        }
    }

fun main() {
    measureTimeMillis {
        runBlocking {
            val ctx = newSingleThreadContext("MyOwnThread")
            repeat(10) {
//                suspendingCall(ctx)
                blockingCall(ctx)
            }
        }
    }.also {
        println("Taked $it millis")
    }
}