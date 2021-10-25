package ru.otus.otuskotlin.marketplace.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.*
import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.controllers.KtorUserSession
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import java.time.Duration

data class AppKtorConfig constructor(
    val userSessions: MutableSet<KtorUserSession> = mutableSetOf(),
    val objectMapper: ObjectMapper = jacksonObjectMapper(),
    val adRepoTest: IRepoAd = RepoAdInMemory(initObjects = listOf()),
    val adRepoProd: IRepoAd = RepoAdInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
    val contextConfig: ContextConfig = ContextConfig(
        repoProd = adRepoProd,
        repoTest = adRepoTest,
    ),
    val auth: KtorAuthConfig = KtorAuthConfig.TEST,
    val crud: AdCrud = AdCrud(contextConfig),
    val adService: AdService = AdService(crud),
) {
    constructor(environment: ApplicationEnvironment): this(
        auth = KtorAuthConfig(environment)
    )

    companion object {
    }
}
