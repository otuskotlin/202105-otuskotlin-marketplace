package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel

data class DbAdIdRequest(
    val id: AdIdModel
): IDbRequest
