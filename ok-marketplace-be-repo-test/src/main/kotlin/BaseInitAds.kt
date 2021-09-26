package ru.otus.otuskotlin.marketplace.backend.repo.test

import ru.otus.otuskotlin.marketplace.backend.common.models.*

abstract class BaseInitAds(val op: String): IInitObjects<AdModel> {

    fun createInitTestModel(
        suf: String,
        ownerId: OwnerIdModel = OwnerIdModel("owner-123"),
    ) = AdModel(
        id = AdIdModel("ad-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = AdVisibilityModel.OWNER_ONLY,
        dealSide = DealSideModel.DEMAND,
        permissions = mutableSetOf()
    )
}
