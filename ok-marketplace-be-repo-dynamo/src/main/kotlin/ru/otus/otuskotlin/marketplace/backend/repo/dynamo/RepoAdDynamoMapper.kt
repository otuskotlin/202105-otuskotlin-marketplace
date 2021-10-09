package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.model.AttributeAction
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.AttributeValueUpdate
import ru.otus.otuskotlin.marketplace.backend.common.models.*

// Константы с именами аттрибутов таблицы
internal const val ID = "id"
internal const val TITLE = "title"
internal const val DESCRIPTION = "description"
internal const val OWNER_ID = "owner_id"
internal const val VISIBILITY = "visibility"
internal const val DEAL_SIDE = "deal_side"

// Строка таблицы представляет собой словарь
internal fun Map<String, AttributeValue>.toModel() = AdModel(
    id = (get(ID) as? AttributeValue.S)?.value?.let { AdIdModel(it) }?: AdIdModel.NONE,
    title = (get(TITLE) as? AttributeValue.S)?.value?: "",
    description = (get(DESCRIPTION) as? AttributeValue.S)?.value?: "",
    ownerId = (get(OWNER_ID) as? AttributeValue.S)?.value?.let { OwnerIdModel(it) }?: OwnerIdModel.NONE,
    visibility = (get(VISIBILITY) as? AttributeValue.S)?.value?.let { AdVisibilityModel.valueOf(it) }?: AdVisibilityModel.NONE,
    dealSide = (get(DEAL_SIDE) as? AttributeValue.S)?.value?.let { DealSideModel.valueOf(it) }?: DealSideModel.NONE,
)

internal fun AdModel.toCreateItem(id: String? = null): Map<String, AttributeValue> {
    val map = mutableMapOf<String, AttributeValue>()
    (id?: this.id.asString()).takeIf { it.isNotBlank() }?.let {
        map[ID] = AttributeValue.S(it)
    }
    title.takeIf { it.isNotBlank() }?.let {
        map[TITLE] = AttributeValue.S(title)
    }
    description.takeIf { it.isNotBlank() }?.let {
        map[DESCRIPTION] = AttributeValue.S(description)
    }
    ownerId.takeIf { it != OwnerIdModel.NONE }?.let {
        map[OWNER_ID] = AttributeValue.S(ownerId.asString())
    }
    visibility.takeIf { it != AdVisibilityModel.NONE }?.let {
        map[VISIBILITY] = AttributeValue.S(visibility.name)
    }
    dealSide.takeIf { it != DealSideModel.NONE }?.let {
        map[DEAL_SIDE] = AttributeValue.S(dealSide.name)
    }
    return map.toMap()
}

// Возможно стоит отделить update от create, чтобы была возможность сбрасывать аттрибуты
internal fun AdModel.toUpdateItem() = this.toCreateItem().filter { it.key != ID }.map {
    it.key to AttributeValueUpdate {
        value = it.value
        action = AttributeAction.Put
    }
}.toMap()
