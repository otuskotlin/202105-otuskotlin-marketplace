package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.*
import java.util.*

class RepoAdDynamo: IRepoAd {
    private val ID = "id" to ScalarAttributeType.S
    private val TITLE = "title" to ScalarAttributeType.S
    private val DESCRIPTION = "description" to ScalarAttributeType.S
    private val OWNER_ID = "owner_id" to ScalarAttributeType.S
    private val VISIBILITY = "visibility" to ScalarAttributeType.S
    private val DEAL_SIDE = "deal_side" to ScalarAttributeType.S

    private fun Map<String, AttributeValue>.toModel() = AdModel(
        id = (get(ID.first) as? AttributeValue.S)?.value?.let { AdIdModel(it) }?: AdIdModel.NONE,
        title = (get(TITLE.first) as? AttributeValue.S)?.value?: "",
        description = (get(DESCRIPTION.first) as? AttributeValue.S)?.value?: "",
        ownerId = (get(OWNER_ID.first) as? AttributeValue.S)?.value?.let { OwnerIdModel(it) }?: OwnerIdModel.NONE,
        visibility = (get(VISIBILITY.first) as? AttributeValue.S)?.value?.let { AdVisibilityModel.valueOf(it) }?: AdVisibilityModel.NONE,
        dealSide = (get(DEAL_SIDE.first) as? AttributeValue.S)?.value?.let { DealSideModel.valueOf(it) }?: DealSideModel.NONE,
    )

    private fun createItem(model: AdModel, id: String? = null): Map<String, AttributeValue> {
        val map = mutableMapOf<String, AttributeValue>()
        (id?: model.id.asString()).takeIf { it.isNotBlank() }?.let {
            map[ID.first] = AttributeValue.S(it)
        }
        model.title.takeIf { it.isNotBlank() }?.let {
            map[TITLE.first] = AttributeValue.S(it)
        }
        model.description.takeIf { it.isNotBlank() }?.let {
            map[DESCRIPTION.first] = AttributeValue.S(it)
        }
        model.ownerId.takeIf { it != OwnerIdModel.NONE }?.let {
            map[OWNER_ID.first] = AttributeValue.S(it.asString())
        }
        model.visibility.takeIf { it != AdVisibilityModel.NONE }?.let {
            map[VISIBILITY.first] = AttributeValue.S(it.name)
        }
        model.dealSide.takeIf { it != DealSideModel.NONE }?.let {
            map[DEAL_SIDE.first] = AttributeValue.S(it.name)
        }
        return map
    }

    private val client = DynamoDbClient {
        region = "us-west-2"
    }.apply {
        runBlocking {
            describeTable {
                tableName
            }
            createTable(createTableRequest)
        }
    }
    private val createTableRequest = CreateTableRequest {
        tableName = "table_name"
        attributeDefinitions = listOf(
            AttributeDefinition {
                this.attributeName = "ID"
            }
        )
        keySchema = listOf()
    }
    val value = AttributeValue.S("S")

    override suspend fun create(rq: DbAdModelRequest): DbAdResponse {
        val row = rq.ad.copy(id = AdIdModel(UUID.randomUUID().toString()))
        client.putItem {
            tableName = "table_name"
            item = createItem(row)
        }
        return DbAdResponse(row, true)
    }

    override suspend fun read(rq: DbAdIdRequest): DbAdResponse {
        val model = client.getItem {
            this.attributesToGet = listOf()
            this.key = mapOf("key" to AttributeValue.S(rq.id.asString()))
        }.item?.toModel()?: return DbAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                CommonErrorModel(
                    field = "id",
                    message = "Not Found"
                )
            )
        )
        return DbAdResponse(null, true)
    }

    override suspend fun update(rq: DbAdModelRequest): DbAdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun delete(rq: DbAdIdRequest): DbAdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun search(rq: DbAdFilterRequest): DbAdsResponse {
        TODO("Not yet implemented")
    }
}
