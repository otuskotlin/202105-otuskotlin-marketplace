package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.DealSideModel
import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel

data class DbAdFilterRequest(
    val searchStr: String = "",
    val ownerId: OwnerIdModel = OwnerIdModel.NONE,
    val dealSide: DealSideModel = DealSideModel.NONE,
): IDbRequest {
    companion object {
        val NONE = DbAdFilterRequest()
    }
}
