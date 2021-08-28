package ru.otus.otuskotlin.marketplace.common.cor.handlers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.otus.otuskotlin.marketplace.common.cor.*

fun <T> ICorChainDsl<T>.parallel(function: CorParallelDsl<T>.() -> Unit) {
    add(CorParallelDsl<T>().apply(function))
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
