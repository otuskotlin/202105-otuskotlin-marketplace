package ru.otus.otuskotlin.marketplace.backend.common.models

data class AdModel(
    val id: AdIdModel = AdIdModel.NONE,
    val title: String = "",
    val description: String = "",
    val ownerId: OwnerIdModel = OwnerIdModel.NONE,
    val visibility: AdVisibilityModel = AdVisibilityModel.NONE,
    val dealSide: DealSideModel = DealSideModel.NONE,
    val permissions: MutableSet<PermissionModel> = mutableSetOf(),
)
