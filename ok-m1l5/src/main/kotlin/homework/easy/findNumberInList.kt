package homework.easy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


suspend fun findNumberInList(toFind: Int, numbers: List<Int>): Int =
    withContext(Dispatchers.Default) {
//        Thread.sleep(2000L)
        delay(2000L)
        val result = numbers.firstOrNull { it == toFind } ?: -1
        return@withContext result
    }


suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}



