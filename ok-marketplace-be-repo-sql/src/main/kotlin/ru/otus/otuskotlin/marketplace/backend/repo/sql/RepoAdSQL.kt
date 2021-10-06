package ru.otus.otuskotlin.marketplace.backend.repo.sql

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.backend.repo.common.*
import ru.otus.otuskotlin.marketplace.backend.repo.sql.tables.AdTable
import java.sql.SQLException
import java.util.*

class RepoAdSQL(initObjects: Collection<AdModel> = emptyList()) : IRepoAd {
    private val db by lazy { SqlConnector().connect(AdTable) }

    init {
        runBlocking {
            initObjects.forEach {
                val res = save(it)
                println(res)
            }
        }
    }

    private suspend fun save(item: AdModel): DbAdResponse {
        return safeTransaction({
            val res = AdTable.insert {
                it[id] = item.id.asString()
                it[title] = item.title
                it[description] = item.description
                it[ownerId] = item.ownerId.id
                it[visibility] = item.visibility
                it[dealSide] = item.dealSide
            }

            DbAdResponse(AdTable.from(res), true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(CommonErrorModel(message = localizedMessage))
            )
        })
    }


    override suspend fun create(req: DbAdModelRequest): DbAdResponse {
        val id = if (req.ad.id != AdIdModel.NONE) req.ad.id else AdIdModel(UUID.randomUUID().toString())
        return save(req.ad.copy(id = id))
    }

    override suspend fun read(req: DbAdIdRequest): DbAdResponse {
        return safeTransaction({
            val result = AdTable.select { AdTable.id.eq(req.id.asString()) }.single()

            DbAdResponse(AdTable.from(result), true)
        }, {
            val err = when (this) {
                is NoSuchElementException -> CommonErrorModel(field = "id", message = "Not Found")
                is IllegalArgumentException -> CommonErrorModel(message = "More than one element with the same id")
                else -> CommonErrorModel(message = localizedMessage)
            }
            DbAdResponse(result = null, isSuccess = false, errors = listOf(err))
        })
    }

    override suspend fun update(req: DbAdModelRequest): DbAdResponse {
        val ad = req.ad
        return safeTransaction({
            AdTable.update({ AdTable.id.eq(ad.id.asString()) }) {
                it[title] = ad.title
                it[description] = ad.description
                it[ownerId] = ad.ownerId.id
                it[visibility] = ad.visibility
                it[dealSide] = ad.dealSide
            }
            val result = AdTable.select { AdTable.id.eq(ad.id.asString()) }.single()

            DbAdResponse(result = AdTable.from(result), isSuccess = true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(CommonErrorModel(field = "id", message = "Not Found"))
            )
        })
    }

    override suspend fun delete(req: DbAdIdRequest): DbAdResponse {
        return safeTransaction({
            val result = AdTable.select { AdTable.id.eq(req.id.asString()) }.single()
            AdTable.deleteWhere { AdTable.id eq req.id.asString() }

            DbAdResponse(result = AdTable.from(result), isSuccess = true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(CommonErrorModel(field = "id", message = "Not Found"))
            )
        })
    }

    override suspend fun search(req: DbAdFilterRequest): DbAdsResponse {
        return safeTransaction({
            val results = AdTable.select {
                (AdTable.ownerId eq req.ownerId.asString()) or (AdTable.dealSide eq req.dealSide)
            }

            DbAdsResponse(result = results.map { AdTable.from(it) }, isSuccess = true)
        }, {
            DbAdsResponse(result = emptyList(), isSuccess = false, listOf(CommonErrorModel(message = localizedMessage)))
        })
    }

    /**
     * Transaction wrapper to safely handle caught exception and throw all sql-like exceptions. Also remove lot's of duplication code
     */
    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Exception.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            return handleException(e)
        }
    }
}
