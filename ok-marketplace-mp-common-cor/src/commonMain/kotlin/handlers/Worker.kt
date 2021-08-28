package ru.otus.otuskotlin.marketplace.common.cor.handlers

import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.ICorWorker
import ru.otus.otuskotlin.marketplace.common.cor.ICorWorkerDsl
import ru.otus.otuskotlin.marketplace.common.cor.ru.otus.otuskotlin.marketplace.common.cor.CorDslMarker

@CorDslMarker
fun <T> ICorChainDsl<T>.worker(
    function: CorWorkerDsl<T>.() -> Unit
) {
    add(
        CorWorkerDsl<T>().apply(function)
    )
}

@CorDslMarker
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

@CorDslMarker
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
