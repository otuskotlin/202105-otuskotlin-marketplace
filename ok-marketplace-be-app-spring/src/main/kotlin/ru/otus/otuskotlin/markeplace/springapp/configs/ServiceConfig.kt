package ru.otus.otuskotlin.markeplace.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import java.time.Duration

@Configuration
class ServiceConfig {

    @Bean
    fun contextConfig(): ContextConfig = ContextConfig(
        repoProd = RepoAdInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
        repoTest = RepoAdInMemory(initObjects = listOf()),
    )

    @Bean
    fun crud(contextConfig: ContextConfig): AdCrud = AdCrud(contextConfig)

    @Bean
    fun serviceAd(crud: AdCrud): AdService = AdService(crud)


}
