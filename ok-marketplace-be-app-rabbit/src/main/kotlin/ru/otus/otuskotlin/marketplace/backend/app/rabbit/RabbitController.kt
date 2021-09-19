package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.concurrent.Executors

class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) {
    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor()
            .asCoroutineDispatcher() + CoroutineName("thread-kafka-controller")
    )

    fun start() = scope.launch {
        processors.forEach {
            launch(Executors.newSingleThreadExecutor()
                .asCoroutineDispatcher() + CoroutineName("thread-${it.consumerTag}")) {
                try {
                    it.process()
                } catch (e: RuntimeException) {
                    // логируем, что-то делаем
                    e.printStackTrace()
                }
            }
        }
    }
}
