package ru.otus.otuskotlin.markeplace.app.serverless

import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import java.time.Duration

val contextConfig: ContextConfig = ContextConfig(
    repoProd = RepoAdInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
    repoTest = RepoAdInMemory(initObjects = listOf()),
)
val crud: AdCrud = AdCrud(contextConfig)
val adService = AdService(crud)
