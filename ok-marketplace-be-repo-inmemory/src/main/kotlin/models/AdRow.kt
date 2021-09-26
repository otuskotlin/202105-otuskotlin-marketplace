package ru.otus.otuskotlin.marketplace.backend.repo.inmemory.models

import ru.otus.otuskotlin.marketplace.backend.common.models.*
import java.io.Serializable

data class AdRow(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val visibility: String? = null,
    val dealSide: String? = null,
): Serializable {
    constructor(internal: AdModel): this(
        id = internal.id.asString().takeIf { it.isNotBlank() },
        title = internal.title.takeIf { it.isNotBlank() },
        description = internal.description.takeIf { it.isNotBlank() },
        ownerId = internal.ownerId.takeIf { it != OwnerIdModel.NONE }?.asString(),
        visibility = internal.visibility.takeIf { it != AdVisibilityModel.NONE }?.name,
        dealSide = internal.dealSide.takeIf { it != DealSideModel.NONE }?.name,
    )

    fun toInternal(): AdModel = AdModel(
        id = id?.let { AdIdModel(it) } ?: AdIdModel.NONE,
        title = title ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { OwnerIdModel(it) } ?: OwnerIdModel.NONE,
        visibility = visibility?.let { AdVisibilityModel.valueOf(it) } ?: AdVisibilityModel.NONE,
        dealSide = dealSide?.let { DealSideModel.valueOf(it) } ?: DealSideModel.NONE,
    )
}
