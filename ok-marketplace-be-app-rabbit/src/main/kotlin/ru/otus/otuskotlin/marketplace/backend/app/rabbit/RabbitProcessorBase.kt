package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class RabbitProcessorBase(
    private val config: RabbitConfig,
    val consumerTag: String,
) {
    private val connectionString = "amqp://${config.user}:${config.password}@${config.host}:${config.port}"
    private val factory = ConnectionFactory()

    suspend fun process() {
        withContext(Dispatchers.IO) {
            ConnectionFactory().apply {
                host = config.host
                port = config.port
            }.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    val deliveryCallback = channel.getDeliveryCallback()
                    val cancelCallback = channel.getCancelCallback()
                    channel.listen(deliveryCallback, cancelCallback)
                }
            }
        }
    }

    protected abstract fun Channel.getDeliveryCallback(): DeliverCallback

    protected abstract fun Channel.getCancelCallback(): CancelCallback

    protected abstract fun Channel.listen(deliverCallback: DeliverCallback, cancelCallback: CancelCallback)
}

