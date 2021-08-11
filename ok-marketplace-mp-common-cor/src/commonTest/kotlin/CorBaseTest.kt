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
            }

            chain {
                on { status == CorStatuses.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some = 0
                }

                + {
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
        }
    }
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
