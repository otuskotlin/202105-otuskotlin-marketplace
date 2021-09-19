package ru.otus.otuskotlin.marketplace.app.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.marketplace.openapi.models.*
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig(
            kafkaConsumer = consumer,
            kafkaProducer = producer,
        )
        val app = AppKafkaConsumer(config)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(config.kafkaTopicIn, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    config.kafkaTopicIn,
                    PARTITION,
                    0L,
                    "test-1",
                    CreateAdRequest(
                        requestId = "123",
                        createAd = CreateableAd(
                            title = "Some Ad",
                            description = "some testing ad to check them all",
                            ownerId = "345",
                            visibility = AdVisibility.OWNER_ONLY,
                            dealSide = AdDealSide.DEMAND,
                            product = AdProductBolt(
                                lengh = 22.0,
                                diameter = 8.0,
                                threadPitch = 1.25
                            )
                        ),
                        debug = BaseDebugRequest(
                            mode = BaseDebugRequest.Mode.STUB,
                            stubCase = BaseDebugRequest.StubCase.SUCCESS
                        )
                    ).toJson()
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
//        val startOffsets = HashMap<TopicPartition, Long>()
        val tp = TopicPartition(config.kafkaTopicIn, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = message.value().fromJson<CreateAdResponse>()
        assertEquals("123", result.requestId)
        assertEquals("Some Ad", result.createdAd?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}

private val om = ObjectMapper()
private fun BaseMessage.toJson(): String = om.writeValueAsString(this)
private inline fun <reified T: BaseMessage> String.fromJson() = om.readValue<T>(this, T::class.java)

