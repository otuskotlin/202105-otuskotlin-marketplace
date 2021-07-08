import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val coldFlow = flow<Int> { println("I'm cold") }
    launch { coldFlow.collect() }
    launch { coldFlow.collect() }


    val hotFlow = flow<Int> { println("I'm hot") }.shareIn(this, SharingStarted.Lazily)
    launch { hotFlow.collect() }
    launch { hotFlow.collect() }
}