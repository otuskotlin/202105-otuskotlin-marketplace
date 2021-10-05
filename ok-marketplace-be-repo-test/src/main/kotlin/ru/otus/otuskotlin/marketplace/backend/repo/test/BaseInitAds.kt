package ru.otus.otuskotlin.marketplace.backend.repo.test

import ru.otus.otuskotlin.marketplace.backend.common.models.*
import java.util.*

abstract class BaseInitAds : IInitObjects<AdModel> {

    fun createInitTestModel(
        suf: String,
        ownerId: OwnerIdModel = OwnerIdModel(UUID.randomUUID()),
        dealSide: DealSideModel = DealSideModel.DEMAND,
    ) = AdModel(
        id = AdIdModel(UUID.randomUUID()),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = AdVisibilityModel.OWNER_ONLY,
        dealSide = dealSide,
    )
}
