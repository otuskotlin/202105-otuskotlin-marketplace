import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.FileWriter
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.concurrent.schedule

data class Sample(
    val serialNumber: String,
    val value: Double,
    val timestamp: Instant = Instant.now()
)

interface Detector {
    fun samples(): Flow<Sample>
}

class CoroutineDetector(
    private val serialNumber: String,
    private val sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long
) : Detector {
    override fun samples(): Flow<Sample> =
        flow {
            val values = sampleDistribution.iterator()
            while (true) {
                emit(Sample(serialNumber, values.next()))
                delay(samplePeriod)
            }
        }
}

class BlockingDetector(
    private val serialNumber: String,
    private val sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long
) : Detector {
    override fun samples(): Flow<Sample> =
        flow {
            val values = sampleDistribution.iterator()
            while (true) {
                emit(Sample(serialNumber, values.next()))
                Thread.sleep(samplePeriod)
            }
        }.flowOn(Dispatchers.IO)
}

class CallbackDetector(
    private val serialNumber: String,
    private val sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long
) : Detector {
    override fun samples(): Flow<Sample> =
        callbackFlow {
            val values = sampleDistribution.iterator()
            val timer = Timer()
            timer.schedule(0L, samplePeriod) {
                trySendBlocking(Sample(serialNumber, values.next()))
            }
            timer.schedule(7_000L) { close(RuntimeException("Detector request failed")) }
            awaitClose { timer.cancel() }
        }
}

fun <T> Flow<T>.rollingMax(comparator: Comparator<T>): Flow<T> =
    runningReduce { max, current -> maxOf(max, current, comparator) }

suspend fun main(): Unit = coroutineScope {
    val distribution = BoxMullerDistribution().scale(24.0, 1.5)
    val detectors = listOf(
        CoroutineDetector("coroutine", distribution, 500L),
        BlockingDetector("blocking", distribution, 1_000L),
        CallbackDetector("callback", distribution, 2_000L)
    )

    val desiredPeriod = 1_000L
    val samples = detectors
        .map {
            it.samples()
                .retry()
                .transformLatest {
                    emit(it)
                    while (true) {
                        delay(desiredPeriod)
                        emit(it.copy(timestamp = Instant.now()))
                    }
                }
                .sample(desiredPeriod)
        }
        .merge()
        .shareIn(this, SharingStarted.Lazily)

    samples
        .rollingMax(compareBy { it.value })
        .sample(desiredPeriod)
        .onEach { println(it) }
        .launchIn(this)

    withTimeoutOrNull(10_500L) {
        withContext(Dispatchers.IO) {
            FileWriter("samples.csv").use { writer ->
                println("Start collecting samples")
                try {
                    samples.collect {
                        writer.append(it.serialNumber.padEnd(10))
                        writer.append(";")
                        writer.append(it.timestamp.atZone(ZoneId.systemDefault()).toLocalTime().toString())
                        writer.append(";")
                        writer.append(it.value.toString())
                        writer.appendLine()
                    }
                } finally {
                    println("Stop collecting samples")
                }
            }
        }
    }

    coroutineContext.cancelChildren()
}