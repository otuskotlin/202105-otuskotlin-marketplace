package ru.otus.otuskotlin.marketplace.common.cor

import kotlin.test.Test

class CorBaseTest {
    @Test
    fun createCor() {
        val chain = chain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == CorStatuses.NONE }
                exec { status = CorStatuses.RUNNING }
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

                +{
                    some += 10
                }
            }

            parallel {
                on {
                    some < 15
                }

                exec {
                    some++
                }
            }
            printResult()

        }.build()
    }
}

private fun <T> chain(block: CorChainDsl<T>.() -> Unit): ICorChainBuilder<T> = CorChainDsl<T>().apply(block)

class CorChainDsl<T>(
    val workers: List<ICorWorkerBuilder<T>>
) : ICorChainBuilder<T> {
    override fun build(): ICorWorker<T> = CorChain<T>(workers.map { build() })
    override fun worker(
        title: String,
        description: String,
        block: ICorChainBuilder<T>.() -> Unit
    ) = CorWorkerBuilder<T>(
        title = title,
        description = description,
        block = block
    )

}

class CorWorkerBuilder<T>(
    var title: String = "", 
    var description: String = "", 
    var block: ICorChainBuilder<T>.() -> Unit
): ICorWorkerBuilder<T> {
    override fun build(): ICorWorker<T> = CorWorker<T>

}

class CorWorker<T> : ICorWorker<T> {

}

class CorChain<T>(
    val workers: List<ICorWorker<T>>
) : ICorWorker<T> {
    override suspend fun exec(ctx: T) {
        workers.forEach {
            it.exec(ctx)
        }
    }

}

interface ICorWorkerBuilder<T> {
    fun build(): ICorWorker<T>
}

interface ICorChainBuilder<T> : ICorWorkerBuilder<T> {
    fun worker(
        title: String = "",
        description: String = "",
        block: ICorChainBuilder<T>.() -> Unit = {}
    )
}

interface ICorWorker<T> {
    suspend fun exec(ctx: T)
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
