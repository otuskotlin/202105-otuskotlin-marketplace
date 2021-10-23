package ru.otus.otuskotlin.marketplace.stubs

import ru.otus.otuskotlin.marketplace.backend.common.models.*

object Bolt {
    private val stubReady = AdModel(
        id = AdIdModel(id = "11111111-1111-1111-1111-111111111666"),
        title = "Болт наружний",
        description = "Лучшего болта вы в мире не найдёте",
        ownerId = OwnerIdModel(id = "00560000-0000-0000-0000-000000000001"),
        visibility = AdVisibilityModel.PUBLIC,
        dealSide = DealSideModel.DEMAND,
        permissions = mutableSetOf(PermissionModel.READ)
    )

    private val stubInProgrss = AdModel(
        id = AdIdModel(id = "11111111-1111-1111-1111-111112345678"),
        title = "Пока не знаю какой болт",
        description = "Еще не придумал описание",
        ownerId = OwnerIdModel(id = "00560000-0000-0000-0000-000000000001"),
        visibility = AdVisibilityModel.OWNER_ONLY,
        dealSide = DealSideModel.PROPOSAL,
        permissions = mutableSetOf(PermissionModel.NONE)
    )

    fun getModel(model: (AdModel.() -> Unit)? = null) = stubReady.copy().also { stub ->
        model?.let { stub.apply(it) }
    }

    fun isCorrectId(id: String) = id == "666"

    fun getModels() = listOf(
        stubReady,
        stubInProgrss
    )

    fun AdModel.update(updateableAd: AdModel) = apply {
        title = updateableAd.title
        description = updateableAd.description
        ownerId = updateableAd.ownerId
        visibility = updateableAd.visibility
        dealSide = updateableAd.dealSide
        permissions.addAll(updateableAd.permissions)
    }
}
