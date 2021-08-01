package ru.otus.otuskotlin.marketplace.backend.common.models

data class AdModel(
    var id: AdIdModel = AdIdModel.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: OwnerIdModel = OwnerIdModel.NONE,
    var visibility: AdVisibilityModel = AdVisibilityModel.NONE,
    var dealSide: DealSideModel = DealSideModel.NONE,
    var permissions: MutableSet<PermissionModel> = mutableSetOf(),
)
