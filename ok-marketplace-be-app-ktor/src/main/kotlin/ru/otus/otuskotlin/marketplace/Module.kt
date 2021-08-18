package ru.otus.otuskotlin.marketplace

import org.koin.dsl.module
import ru.otus.otuskotlin.marketplace.services.AdService
import ru.otus.otuskotlin.marketplace.services.AdServiceInterface
import ru.otus.otuskotlin.marketplace.services.OfferService
import ru.otus.otuskotlin.marketplace.services.OfferServiceInterface

val marketplace = module {
    single<AdServiceInterface> { AdService() }
    single<OfferServiceInterface> { OfferService() }
}