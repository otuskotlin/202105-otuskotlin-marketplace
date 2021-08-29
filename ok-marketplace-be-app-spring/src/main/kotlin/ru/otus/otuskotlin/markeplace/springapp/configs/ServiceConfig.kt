package ru.otus.otuskotlin.markeplace.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud

@Configuration
class ServiceConfig {
    @Bean
    fun serviceAd(): AdService = AdService(AdCrud())

}
