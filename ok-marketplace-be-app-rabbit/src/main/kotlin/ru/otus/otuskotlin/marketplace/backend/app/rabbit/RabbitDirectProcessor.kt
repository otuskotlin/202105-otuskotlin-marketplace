package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toInitResponse
import ru.otus.otuskotlin.marketplace.openapi.models.*
import java.time.Instant
import java.util.*

class RabbitDirectProcessor(
    config: RabbitConfig,
    private val keyIn: String,
    private val keyOut: String,
    private val exchange: String,
    private val queue: String,
    private val service: AdService,
    val consumerTag: String = "",
){
    private val connectionString = "amqp://${config.user}:${config.password}@${config.host}:${config.port}"
    private val factory = ConnectionFactory()
    private val jacksonMapper = ObjectMapper()

    suspend fun process() {
        withContext(Dispatchers.IO){
            factory.newConnection(connectionString).use { connection ->
                val channel = connection.createChannel()
                val deliveryCallback = channel.getDeliveryCallback()
                val cancelCallback = channel.getCancelCallback()
                channel.exchangeDeclare(exchange, "direct")
                channel.queueDeclare(queue, false, false, false, null)
                channel.queueBind(queue, exchange, keyIn)
                channel.basicConsume(queue, true, consumerTag, deliveryCallback, cancelCallback)
                while (channel.isOpen) {}
                println("Channel for [$consumerTag] was closed.")
            }
        }
    }

    private fun Channel.getDeliveryCallback(): DeliverCallback  {
        val channel = this
        return DeliverCallback { tag, message ->
            runBlocking {
                val context = MpContext(
                    startTime = Instant.now()
                )
                try {
                    when (val query = jacksonMapper.readValue(message.body, BaseMessage::class.java)) {
                        is InitAdRequest -> {
                            val response = service.initAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        is CreateAdRequest -> {
                            val response = service.createAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        is ReadAdRequest -> {
                            val response = service.readAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        is UpdateAdRequest -> {
                            val response = service.updateAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        is DeleteAdRequest -> {
                            val response = service.deleteAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        is SearchAdRequest -> {
                            val response = service.searchAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        is OffersAdRequest -> {
                            val response = service.offersAd(context, query)
                            jacksonMapper.writeValueAsBytes(response)
                        }
                        else -> {
                            println(query)
                            null
                        }// тут должно отдаваться сообщение с ошибкой}
                    }?.also {
                        channel.basicPublish(exchange, keyOut, null, it)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    context.status = CorStatus.ERROR
                    context.addError(e = e)
                    val response = context.toInitResponse()
                    jacksonMapper.writeValueAsBytes(response).also {
                        channel.basicPublish(exchange, keyOut, null, it)
                    }
                }
            }
        }
    }

    private fun Channel.getCancelCallback() = CancelCallback {
        println("[$it] was cancelled")
    }
}
