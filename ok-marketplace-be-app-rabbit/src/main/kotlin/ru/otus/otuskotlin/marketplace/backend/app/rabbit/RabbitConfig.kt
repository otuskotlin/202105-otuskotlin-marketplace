package ru.otus.otuskotlin.marketplace.backend.app.rabbit

class RabbitConfig(
    val host: String = HOST,
    val port: Int = PORT,
    val user: String = USER,
    val password: String = PASSWORD,
) {
    companion object {
        const val HOST = "localhost"
        const val PORT = 5672
        const val USER = "guest"
        const val PASSWORD = "guest"
    }
}
