package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.DealSideModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpSearchFilter
import ru.otus.otuskotlin.marketplace.backend.common.models.MpSearchTypes
import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel

data class DbAdFilterRequest(
    val searchStr: String = "",
    val ownerId: OwnerIdModel = OwnerIdModel.NONE,
    val dealSide: DealSideModel = DealSideModel.NONE,
    val searchTypes: Set<MpSearchTypes> = setOf(),
): IDbRequest {

    companion object {
        val NONE = DbAdFilterRequest()
        fun of(sf: MpSearchFilter) = DbAdFilterRequest(
            searchStr = sf.searchStr,
            ownerId = sf.ownerId,
            dealSide = sf.dealSide,
            searchTypes = sf.searchTypes.toSet()
        )
    }
}
