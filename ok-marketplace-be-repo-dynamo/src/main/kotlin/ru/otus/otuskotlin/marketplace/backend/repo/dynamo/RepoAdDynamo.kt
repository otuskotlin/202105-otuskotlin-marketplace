package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.*
import java.io.EOFException
import java.util.*

class RepoAdDynamo(
    private val timeout: Long = 30000,
    private val table: String = "table_ads",
    private val index: String = "BY_OWNER_DEALSIDE",
    region: String = "us-west-2",
    initObjects: Collection<AdModel> = emptyList(),
): IRepoAd {

    private val client by lazy {
        DynamoDbClient {
            this.region = region
        }
    }

    /**
     * Запрос на создание таблицы
     * @param tableName - имя таблицы
     * @param attributeDefinitions - аттрибуты, которые указываются в качестве ключей
     * @param keySchema - партиционный и сортировочный ключи таблицы
     * @param globalSecondaryIndexes - список глобальных индексов
     * @param provisionedThroughput - параметры пропускной способности
     */
    private val createTableRequest by lazy {
        CreateTableRequest {
            tableName = table
            attributeDefinitions = listOf(
                AttributeDefinition {
                    attributeName = ID
                    attributeType = ScalarAttributeType.S
                },
                AttributeDefinition {
                    attributeName = TITLE
                    attributeType = ScalarAttributeType.S
                },
                AttributeDefinition {
                    attributeName = OWNER_ID
                    attributeType = ScalarAttributeType.S
                },
                AttributeDefinition {
                    attributeName = DEAL_SIDE
                    attributeType = ScalarAttributeType.S
                }
            )
            keySchema = listOf(
                KeySchemaElement {
                    attributeName = ID
                    keyType = KeyType.Hash
                },
                KeySchemaElement {
                    attributeName = TITLE
                    keyType = KeyType.Range
                },
            )
            provisionedThroughput = ProvisionedThroughput {
                readCapacityUnits = 10
                writeCapacityUnits = 10
            }
            globalSecondaryIndexes = listOf(
                GlobalSecondaryIndex {
                    indexName = index
                    keySchema = listOf(
                        KeySchemaElement {
                            attributeName = OWNER_ID
                            keyType = KeyType.Hash
                        },
                        KeySchemaElement {
                            attributeName = DEAL_SIDE
                            keyType = KeyType.Range
                        }
                    )
                    projection = Projection {
                        projectionType = ProjectionType.Include
                        this.nonKeyAttributes = listOf(
                            ID,
                            TITLE,
                            DESCRIPTION,
                            VISIBILITY
                        )
                    }
                    provisionedThroughput = ProvisionedThroughput {
                        readCapacityUnits = 10
                        writeCapacityUnits = 10
                    }
                }
            )
        }
    }

    init {
        runBlocking {
            // Если таблица не существует, то вызывается EOFException, в обработчике создается таблица
            try {
                client.describeTable {
                    tableName = table
                }
            } catch (e: EOFException) {
                println(e.localizedMessage)
                client.createTable(createTableRequest)
            }
            var tableIsActive = client.describeTable {
                tableName = table
            }.table?.tableStatus == TableStatus.Active
            // Ожидаем пока статус таблицы не изменится на Active
            while (!tableIsActive) {
                delay(500)
                tableIsActive = client.describeTable {
                    tableName = table
                }.table?.tableStatus == TableStatus.Active
            }
            println("Table was created")
            initObjects.forEach {
                save(it)
            }
        }
    }

    override suspend fun create(req: DbAdModelRequest): DbAdResponse {
        val row = req.ad.copy(id = AdIdModel(UUID.randomUUID().toString()))
        return save(row)
    }

    override suspend fun read(req: DbAdIdRequest): DbAdResponse {
        return doWithTimeout({
            if (req.id == AdIdModel.NONE)
                return@doWithTimeout DbAdResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(
                        CommonErrorModel(
                            field = "id",
                            message = "Id is empty"
                        )
                    )
                )
            val keyToGet = mutableMapOf<String, AttributeValue>()
            keyToGet[ID] = AttributeValue.S(req.id.asString())
            keyToGet[TITLE] = AttributeValue.S("first-ad")
            val row = client.getItem {
                tableName = table
                key = keyToGet
            }.item?.toModel()?: return@doWithTimeout DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        field = "id",
                        message = "Not Found"
                    )
                )
            )
            DbAdResponse(row, true)
        },{
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        message = localizedMessage,
                        field = "RepoAdDynamo.read",
                        exception = this
                    )
                )
            )
        })

    }

    override suspend fun update(req: DbAdModelRequest): DbAdResponse {
        return doWithTimeout({
            val keyToUpd = mapOf(ID to AttributeValue.S(req.ad.id.asString()))
            client.updateItem {
                tableName = table
                key = keyToUpd
                attributeUpdates = req.ad.toUpdateItem()
            }
            DbAdResponse(req.ad, true)
        },{
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        message = localizedMessage,
                        field = "RepoAdDynamo.update",
                        exception = this
                    )
                )
            )
        })
    }

    override suspend fun delete(req: DbAdIdRequest): DbAdResponse {
        return doWithTimeout({
            if (req.id == AdIdModel.NONE)
                return@doWithTimeout DbAdResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(
                        CommonErrorModel(
                            field = "id",
                            message = "Id is empty"
                        )
                    )
                )
            val keyToDel = mapOf(ID to AttributeValue.S(req.id.asString()))
            val row = client.getItem {
                tableName = table
                this.key = keyToDel
            }.item?.toModel()?: return@doWithTimeout DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        field = "id",
                        message = "Not Found"
                    )
                )
            )
            client.deleteItem {
                tableName = table
                key = keyToDel
            }
            DbAdResponse(row, true)
        },{
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        message = localizedMessage,
                        field = "RepoAdDynamo.delete",
                        exception = this
                    )
                )
            )
        })
    }

    override suspend fun search(req: DbAdFilterRequest): DbAdsResponse {
        return doWithTimeout({
            val rows = client.scan {
                tableName = table
                indexName = index
                expressionAttributeNames = mapOf(
                    "#owner_id" to OWNER_ID,
                    "#deal_side" to DEAL_SIDE
                )
                expressionAttributeValues = mapOf(
                    ":owner_id" to AttributeValue.S(req.ownerId.asString()),
                    ":deal_side" to AttributeValue.S(req.dealSide.name)
                )
                filterExpression = "#owner_id = :owner_id OR #deal_side = :deal_side"
            }.items?.asFlow()?.map { it.toModel() }?.toList()?: emptyList()
            DbAdsResponse(rows, true)
        },{
            DbAdsResponse(
                result = emptyList(),
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        message = localizedMessage,
                        field = "RepoAdDynamo.search",
                        exception = this
                    )
                )
            )
        })

    }

    suspend fun clean() {
        client.deleteTable {
            tableName = table
        }
        println("Table was deleted")
    }

    private suspend fun save(model: AdModel): DbAdResponse {
        return doWithTimeout({
            client.putItem {
                tableName = table
                item = model.toCreateItem()
            }
            DbAdResponse(model, true)
        },{
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        message = localizedMessage,
                        field = "RepoAdDynamo.create",
                        exception = this
                    )
                )
            )
        })
    }

    private suspend fun <T> doWithTimeout(block: suspend () -> T, onError: Throwable.() -> T) =
        try {
            withTimeout(timeout) {
                block()
            }
        } catch (e: Throwable) {
            onError(e)
        }
}
