package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.model.AttributeAction
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.AttributeValueUpdate
import ru.otus.otuskotlin.marketplace.backend.common.models.*

data class AdDynamoDto(
    val id: DynamoAttrString? = null,
    val title: DynamoAttrString? = null,
    val description: DynamoAttrString? = null,
    val ownerId: DynamoAttrString? = null,
    val visibility: DynamoAttrString? = null,
    val dealSide: DynamoAttrString? = null,
) {
    companion object {
        // Константы с именами аттрибутов таблицы
        internal const val ID = "id"
        internal const val TITLE = "title"
        internal const val DESCRIPTION = "description"
        internal const val OWNER_ID = "owner_id"
        internal const val VISIBILITY = "visibility"
        internal const val DEAL_SIDE = "deal_side"
    }
    // конструктор для получения dto из строки таблицы
    constructor(item: Map<String, AttributeValue>): this(
        id = item[ID]?.let {
            DynamoAttrString(ID, it)
        },
        title = item[TITLE]?.let {
            DynamoAttrString(TITLE, it)
        },
        description = item[DESCRIPTION]?.let {
            DynamoAttrString(DESCRIPTION, it)
        },
        ownerId = item[OWNER_ID]?.let {
            DynamoAttrString(OWNER_ID, it)
        },
        visibility = item[VISIBILITY]?.let {
            DynamoAttrString(VISIBILITY, it)
        },
        dealSide = item[DEAL_SIDE]?.let {
            DynamoAttrString(DEAL_SIDE, it)
        },
    )

    constructor(model: AdModel): this(
        id = model.id.takeIf { it != AdIdModel.NONE }?.asString()?.let {
            DynamoAttrString(ID, it)
        },
        title = model.title.takeIf { it.isNotBlank() }?.let {
            DynamoAttrString(TITLE, it)
        },
        description = model.description.takeIf { it.isNotBlank() }?.let {
            DynamoAttrString(DESCRIPTION, it)
        },
        ownerId = model.ownerId.takeIf { it != OwnerIdModel.NONE }?.asString()?.let {
            DynamoAttrString(OWNER_ID, it)
        },
        visibility = model.visibility.takeIf { it != AdVisibilityModel.NONE }?.name?.let {
            DynamoAttrString(VISIBILITY, it)
        },
        dealSide = model.dealSide.takeIf { it != DealSideModel.NONE }?.name?.let {
            DynamoAttrString(DEAL_SIDE, it)
        },
    )

    fun toModel() = AdModel(
        id = id?.value?.let { AdIdModel(it) }?: AdIdModel.NONE,
        title = title?.value?: "",
        description = description?.value?: "",
        ownerId = ownerId?.value?.let { OwnerIdModel(it) }?: OwnerIdModel.NONE,
        visibility = visibility?.value?.let { AdVisibilityModel.valueOf(it) }?: AdVisibilityModel.NONE,
        dealSide = dealSide?.value?.let { DealSideModel.valueOf(it) }?: DealSideModel.NONE,
    )

    // Строка таблицы представляет собой словарь
    fun toCreateItem(): Map<String, AttributeValue> {
        val map = mutableMapOf<String, AttributeValue>()
        id?.attrValue?.let {
            map[ID] = it
        }
        title?.attrValue?.let {
            map[TITLE] = it
        }
        description?.attrValue?.let {
            map[DESCRIPTION] = it
        }
        ownerId?.attrValue?.let {
            map[OWNER_ID] = it
        }
        visibility?.attrValue?.let {
            map[VISIBILITY] = it
        }
        dealSide?.attrValue?.let {
            map[DEAL_SIDE] = it
        }
        return map.toMap()
    }

    fun toUpdateItem() = toCreateItem().filter { it.key != ID }.map {
        it.key to AttributeValueUpdate {
            value = it.value
            action = AttributeAction.Put
        }
    }.toMap()
}
