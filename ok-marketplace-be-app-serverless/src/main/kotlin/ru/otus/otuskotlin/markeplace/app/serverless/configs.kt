package ru.otus.otuskotlin.markeplace.app.serverless

import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logics.AdCrud

val adService = AdService(AdCrud())
