package ru.otus.otuskotlin.marketplace.app.kafka

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import java.time.Duration
import java.util.*

data class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaTopicIn: String = KAFKA_TOPIC_IN,
    val kafkaTopicOut: String = KAFKA_TOPIC_OUT,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val contextConfig: ContextConfig = ContextConfig(
        repoProd = RepoAdInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
        repoTest = RepoAdInMemory(initObjects = listOf()),
    ),
    val crud: AdCrud = AdCrud(contextConfig),
    val kafkaConsumer: Consumer<String, String> = kafkaConsumer(kafkaHosts, kafkaGroupId),
    val kafkaProducer: Producer<String, String> = kafkaProducer(kafkaHosts),
) {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_VAR = "KAFKA_TOPIC_IN"
        const val KAFKA_TOPIC_OUT_VAR = "KAFKA_TOPIC_OUT"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_TOPIC_IN by lazy { System.getenv(KAFKA_TOPIC_IN_VAR) ?: "marketplace-in" }
        val KAFKA_TOPIC_OUT by lazy { System.getenv(KAFKA_TOPIC_OUT_VAR) ?: "marketplace-out" }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "marketplace" }

        fun kafkaConsumer(hosts: List<String>, groupId: String): KafkaConsumer<String, String> {
            val props = Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
                put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            }
            return KafkaConsumer<String, String>(props)
        }

        fun kafkaProducer(hosts: List<String>): KafkaProducer<String, String> {
            val props = Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            }
            return KafkaProducer<String, String>(props)
        }
    }
}
