package ru.otus.otuskotlin.markeplace.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.services.AdService

@Configuration
class ServiceConfig {
    @Bean
    fun serviceAd(): AdService = AdService()

}
