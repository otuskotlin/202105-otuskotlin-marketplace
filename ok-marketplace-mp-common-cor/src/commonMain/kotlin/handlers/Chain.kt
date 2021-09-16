package ru.otus.otuskotlin.marketplace.common.cor.handlers

import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.ICorExecDsl
import ru.otus.otuskotlin.marketplace.common.cor.ICorWorker
import ru.otus.otuskotlin.marketplace.common.cor.CorDslMarker

@CorDslMarker
fun <T> ICorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    override val title: String,
    override val description: String = "",
    val blockOn: suspend T.() -> Boolean = { true },
    val blockExcept: suspend T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)
    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)

    override suspend fun handle(context: T) {
        execs.forEach { it.exec(context) }
    }
}

@CorDslMarker
class CorChainDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var blockOn: suspend T.() -> Boolean = { true },
    private var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }
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

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}
