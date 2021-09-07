package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import ru.otus.otuskotlin.marketplace.openapi.models.InitAdRequest

fun main() {
    val crud = AdCrud()
    val service = AdService(crud)
    val config = RabbitConfig()
    val processor = RabbitDirectProcessor(
        config = config,
        keyIn = "in-key",
        keyOut = "out-key",
        exchange = "test-exchange",
        queue = "test-queue",
        service = service,
        consumerTag = "test-tag"
    )
    val controller = RabbitController(
        processors = setOf(processor)
    )
    controller.start()
//
//    val inQueue = "test_queue"
//    val outQueue = "answer_queue"
//
//    val factory = ConnectionFactory()
//    val connection = factory.newConnection("amqp://guest:guest@localhost:15672/")
//    val channel = connection.createChannel()
//    val consumerTag = "TestRabbitConsumer"
//
//    channel.queueDeclare(inQueue, false, false, false, null)
//    channel.queueDeclare(outQueue, false, false, false, null)
//
//    val deliverCallback = DeliverCallback { tag, delivery ->
//        val message = String(delivery.body, Charsets.UTF_8)
//        println("[$tag] Received message: $message")
//        val context = MpContext()
//        val response = service.initAd(context, InitAdRequest())
//        channel.basicPublish("", outQueue, null, ObjectMapper().writeValueAsBytes(response))
//    }
//    val cancelCallback = CancelCallback {
//        println("[$it] was cancelled.")
//    }
//
//    channel.basicConsume(inQueue, true, consumerTag, deliverCallback, cancelCallback)
}
