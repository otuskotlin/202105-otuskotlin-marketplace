package ru.otus.otuskotlin.marketplace.backend.app.rabbit

class RabbitConfig(
    val host: String = HOST,
    val port: String = PORT,
    val user: String = USER,
    val password: String = PASSWORD,
) {
    companion object {
        const val HOST = "localhost"
        const val PORT = "15672"
        const val USER = "guest"
        const val PASSWORD = "guest"
    }
}
