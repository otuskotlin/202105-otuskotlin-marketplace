package ru.otus.otuskotlin.marketplace.backend.app.rabbit

import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import java.time.Duration

class RabbitConfig(
    val host: String = HOST,
    val port: Int = PORT,
    val user: String = USER,
    val password: String = PASSWORD,
    val contextConfig: ContextConfig = ContextConfig(
        repoProd = RepoAdInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
        repoTest = RepoAdInMemory(initObjects = listOf()),
    ),
    val crud: AdCrud = AdCrud(contextConfig),
    val service: AdService = AdService(crud),
) {
    companion object {
        const val HOST = "localhost"
        const val PORT = 5672
        const val USER = "guest"
        const val PASSWORD = "guest"
    }
}
