package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel

data class DbAdFilterRequest(
    val searchStr: String = "",
    val ownerId: OwnerIdModel = OwnerIdModel.NONE
): IDbRequest
