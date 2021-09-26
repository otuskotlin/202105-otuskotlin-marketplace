package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel

data class DbAdModelRequest(
    val ad: AdModel
): IDbRequest
