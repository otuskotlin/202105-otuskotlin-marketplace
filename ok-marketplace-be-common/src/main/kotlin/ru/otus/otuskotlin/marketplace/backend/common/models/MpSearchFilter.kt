package ru.otus.otuskotlin.marketplace.backend.common.models

data class MpSearchFilter(
    var searchStr: String = "",
    var ownerId: OwnerIdModel = OwnerIdModel.NONE,
    var dealSide: DealSideModel = DealSideModel.NONE,
    var searchTypes: MutableSet<MpSearchTypes> = mutableSetOf(),
)
