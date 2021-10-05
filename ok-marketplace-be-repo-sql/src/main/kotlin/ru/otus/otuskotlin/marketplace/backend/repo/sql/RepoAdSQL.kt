package ru.otus.otuskotlin.marketplace.backend.repo.sql

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.*
import ru.otus.otuskotlin.marketplace.backend.repo.sql.tables.AdTable
import ru.otus.otuskotlin.marketplace.backend.repo.sql.tables.selectNotDeleted
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
                if (item.id != AdIdModel.NONE) {
                    it[id] = item.id.asUUID()
                }
                it[title] = item.title
                it[description] = item.description
                it[ownerId] = item.ownerId.asUUID()
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
            val result = AdTable.selectNotDeleted {
                AdTable.id.eq(req.id.asUUID()) and (AdTable.isDeleted eq false)
            }.single()

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
            AdTable.update({ AdTable.id.eq(ad.id.asUUID()) }) {
                it[title] = ad.title
                it[description] = ad.description
                it[ownerId] = ad.ownerId.asUUID()
                it[visibility] = ad.visibility
                it[dealSide] = ad.dealSide
            }
            val result = AdTable.select { AdTable.id.eq(ad.id.asUUID()) }.single()

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
            val result = AdTable.select { AdTable.id.eq(req.id.asUUID()) }.single()
            // AdTable.deleteWhere { AdTable.id eq req.id.asUUID() }
            // We do not delete our objects, but mark them with the flag "deleted"
            AdTable.update({ AdTable.id.eq(req.id.asUUID()) }) {
                it[isDeleted] = true
            }

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
            // Select only if options are provided
            val results = AdTable.selectNotDeleted {
                (if (req.ownerId == OwnerIdModel.NONE) Op.TRUE else AdTable.ownerId eq req.ownerId.asUUID()) or
                        (if (req.dealSide == DealSideModel.NONE) Op.TRUE else AdTable.dealSide eq req.dealSide)
            }

            DbAdsResponse(result = results.map { AdTable.from(it) }, isSuccess = true)
        }, {
            DbAdsResponse(result = emptyList(), isSuccess = false, listOf(CommonErrorModel(message = localizedMessage)))
        })
    }

    /**
     * Transaction wrapper to safely handle caught exception and throw all sql-like exceptions. Also remove lot's of duplication code
     */
    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            e.printStackTrace()
            return handleException(e)
        }
    }
}
