package ru.otus.otuskotlin.marketplace.app.kafka

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config)
    consumer.run()
}
