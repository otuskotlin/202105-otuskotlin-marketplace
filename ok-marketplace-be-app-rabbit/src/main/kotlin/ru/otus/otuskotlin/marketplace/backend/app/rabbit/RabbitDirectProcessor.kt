package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toInitResponse
import ru.otus.otuskotlin.marketplace.openapi.models.*
import java.time.Instant

class RabbitDirectProcessor(
    config: RabbitConfig,
    consumerTag: String = "",
    private val keyIn: String,
    private val keyOut: String,
    private val exchange: String,
    private val queue: String,
    private val service: AdService,
) : RabbitProcessorBase(config, consumerTag) {
    private val jacksonMapper = ObjectMapper()

    override fun Channel.getDeliveryCallback(): DeliverCallback {
        val channel = this
        return DeliverCallback { tag, message ->
            runBlocking {
                val context = MpContext(
                    startTime = Instant.now()
                )
                try {
                    val query = jacksonMapper.readValue(message.body, BaseMessage::class.java)
                    val response = service.handleAd(context, query)
                    jacksonMapper.writeValueAsBytes(response).also {
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

    override fun Channel.getCancelCallback() = CancelCallback {
        println("[$it] was cancelled")
    }

    override fun Channel.listen(deliverCallback: DeliverCallback, cancelCallback: CancelCallback) {
        // Объявляем обменник типа "direct" (сообщения передаются в те очереди, где ключ совпадает)
        exchangeDeclare(exchange, "direct")
        // Объявляем очередь (не сохраняется при перезагрузке сервера; неэксклюзивна - доступна другим соединениям;
        // не удаляется, если не используется)
        queueDeclare(queue, false, false, false, null)
        // связываем обменник с очередью по ключу (сообщения будут поступать в данную очередь с данного обменника при совпадении ключа)
        queueBind(queue, exchange, keyIn)
        // запуск консьюмера с автоотправкой подтверждение при получении сообщения
        basicConsume(queue, true, consumerTag, deliverCallback, cancelCallback)
        while (isOpen) {
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        println("Channel for [$consumerTag] was closed.")
    }
}
