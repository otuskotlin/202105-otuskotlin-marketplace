package ru.otus.otuskotlin.marketplace.common.cor

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test

class CorBaseTest {
    @Test
    fun createCor() {
        val chain = chain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == CorStatuses.NONE }
                handle { status = CorStatuses.RUNNING }
                except { status = CorStatuses.ERROR }
            }

            chain {
                on { status == CorStatuses.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some = 0
                }

            }

            parallel {
                on {
                    some < 15
                }

                worker(title = "Increment some") {
                    some++
                }
            }
            printResult()

        }.build()
    }


}

private fun ICorChainDsl<TestContext>.printResult() = worker(title = "Print example") {
    println("some = $some")
}

fun <T> ICorChainDsl<T>.worker(
    function: CorWorkerDsl<T>.() -> Unit
) {
    add(
        CorWorkerDsl<T>().apply(function)
    )
}

fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    function: T.() -> Unit
) {
    add(
        CorWorkerDsl<T>(
            title = title,
            description = description,
            blockHandle = function
        )
    )
}

fun <T> ICorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.parallel(function: CorParallelDsl<T>.() -> Unit) {
    add(CorParallelDsl<T>().apply(function))
}

fun <T> chain(function: CorChainDsl<T>.() -> Unit) = CorChainDsl<T>().apply(function)

class CorChainDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var blockOn: T.() -> Boolean = { true },
    private var blockExcept: T.(e: Throwable) -> Unit = { e: Throwable -> throw e }
) : ICorChainDsl<T> {
    override fun build(): ICorExec<T> = CorChain<T>(
        title = title,
        description = description,
        execs = workers.map { it.build() }.toList(),
        blockOn = blockOn,
        blockExcept = blockExcept
    )

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun on(function: T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}

class CorParallelDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var blockOn: T.() -> Boolean = { true },
    private var blockExcept: T.(e: Throwable) -> Unit = { e: Throwable -> throw e }
): ICorChainDsl<T>, ICorHandlerDsl<T> {
    override fun build(): ICorExec<T> = CorParallel<T>(
        title = title,
        description = description,
        execs = workers.map { it.build() }.toList(),
        blockOn = blockOn,
        blockExcept = blockExcept
    )

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun on(function: T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}

class CorWorkerDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private var blockOn: T.() -> Boolean = { true },
    private var blockHandle: T.() -> Unit = {},
    private var blockExcept: T.(e: Throwable) -> Unit = { e: Throwable -> throw e },
) : ICorWorkerDsl<T> {

    override fun build(): ICorExec<T> = CorWorker<T>(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )

    override fun on(function: T.() -> Boolean) {
        blockOn = function
    }

    override fun handle(function: T.() -> Unit) {
        blockHandle = function
    }

    override fun except(function: T.(e: Throwable) -> Unit) {
        blockExcept = function
    }

}

class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    override val title: String,
    override val description: String = "",
    val blockOn: T.() -> Boolean = { true },
    val blockExcept: T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)
    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)

    override suspend fun handle(context: T) {
        execs.forEach { it.exec(context) }
    }
}

class CorParallel<T>(
    private val execs: List<ICorExec<T>>,
    override val title: String,
    override val description: String = "",
    val blockOn: T.() -> Boolean = { true },
    val blockExcept: T.(Throwable) -> Unit = {},
) : ICorWorker<T> {

    override suspend fun on(context: T): Boolean = blockOn(context)
    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)

    override suspend fun handle(context: T): Unit = coroutineScope {
        execs.map { launch { it.exec(context) } }
    }
}

class CorWorker<T>(
    override val title: String,
    override val description: String = "",
    val blockOn: T.() -> Boolean = { true },
    val blockHandle: T.() -> Unit = {},
    val blockExcept: T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)
    override suspend fun handle(context: T) = blockHandle(context)
    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)
}

interface ICorExecDsl<T> {
    var title: String
    var description: String
    fun build(): ICorExec<T>
}

interface ICorChainDsl<T> : ICorExecDsl<T>, ICorHandlerDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

interface ICorWorkerDsl<T> : ICorExecDsl<T>, ICorHandlerDsl<T> {
    fun handle(function: T.() -> Unit)
}

interface ICorExec<T> {
    suspend fun exec(context: T)
}

interface ICorWorker<T> : ICorExec<T> {
    val title: String
    val description: String
    suspend fun on(context: T): Boolean
    suspend fun except(context: T, e: Throwable)
    suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                except(context, e)
            }
        }
    }
}

interface ICorHandlerDsl<T> {
    fun on(function: T.() -> Boolean)
    fun except(function: T.(e: Throwable) -> Unit)
}

data class TestContext(
    var status: CorStatuses = CorStatuses.NONE,
    var some: Int = Int.MIN_VALUE
) {

}

enum class CorStatuses {
    NONE,
    RUNNING,
    FAILING,
    DONE,
    ERROR
}
