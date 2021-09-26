package ru.otus.otuskotlin.marketplace

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.controllers.KtorUserSession
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import java.time.Duration

data class AppKtorConfig(
    val userSessions: MutableSet<KtorUserSession> = mutableSetOf<KtorUserSession>(),
    val objectMapper: ObjectMapper = jacksonObjectMapper(),
    val adRepoTest: IRepoAd = RepoAdInMemory(initObjects = listOf()),
    val adRepoProd: IRepoAd = RepoAdInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
    val crud: AdCrud = AdCrud(),
    val adService: AdService = AdService(crud),
) {
    companion object {
    }
}
