package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud

fun main() {
    val config = RabbitConfig()
    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            keyIn = "in",
            keyOut = "out",
            exchange = "test-exchange",
            queue = "test-queue",
            service = config.service,
            consumerTag = "test-tag"
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}
