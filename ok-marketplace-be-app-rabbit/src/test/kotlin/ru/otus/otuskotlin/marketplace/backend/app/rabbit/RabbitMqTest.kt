package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import ru.otus.otuskotlin.marketplace.openapi.models.BaseDebugRequest
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdResponse
import ru.otus.otuskotlin.marketplace.openapi.models.CreateableAd
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RabbitMqTest {

    companion object {
        const val keyIn = "key-in"
        const val keyOut = "key-Out"
        const val exchange = "test-exchange"
        const val queue = "test-queue"
        val container by lazy {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
//                withExchange(exchangeIn, "fanout")
//                withQueue(queueIn, false, true, null)
//                withBinding(exchangeIn, queueIn)
//                withExposedPorts(5672, 15672)
                withUser("guest", "guest")
                start()
            }
        }

        val rabbitMqTestPort: Int by lazy {
            container.getMappedPort(5672)
        }
        val config by lazy {
            RabbitConfig(
                port = rabbitMqTestPort
            )
        }
        val crud = AdCrud()
        val service = AdService(crud)
        val processor by lazy {
            RabbitDirectProcessor(
                config = config,
                keyIn = keyIn,
                keyOut = keyOut,
                exchange = exchange,
                queue = queue,
                service = service,
                consumerTag = "test-tag"
            )
        }
        val controller by lazy {
            RabbitController(
                processors = setOf(processor)
            )
        }
        val mapper = ObjectMapper()
    }

    @BeforeTest
    fun tearUp() {
        controller.start()
    }

    @Test
    fun adCreateTest() {
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.getBody(), Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, mapper.writeValueAsBytes(boltCreate))

                runBlocking {
                    withTimeoutOrNull(250L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = mapper.readValue(responseJson, CreateAdResponse::class.java)
                val expected = Bolt.getModel()
                assertEquals(expected.title, response.createdAd?.title)
                assertEquals(expected.description, response.createdAd?.description)

            }
        }
    }

    private val boltCreate = with(Bolt.getModel()){
        CreateAdRequest(
            createAd = CreateableAd(
                title = title,
                description = description
            ),
            debug = BaseDebugRequest(
                mode = BaseDebugRequest.Mode.STUB,
                stubCase = BaseDebugRequest.StubCase.SUCCESS,
            )
        )
    }
}
