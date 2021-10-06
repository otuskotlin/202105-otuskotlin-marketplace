package ru.otus.otuskotlin.marketplace.backend.repo.sql

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.*
import java.sql.SQLException
import java.util.*

class RepoAdSQL(initObjects: Collection<AdModel> = emptyList()) : IRepoAd {
    private val db by lazy { SqlConnector().connect(AdsTable, UsersTable) }

    init {
        runBlocking {
            initObjects.forEach {
                save(it)
            }
        }
    }

    private suspend fun save(item: AdModel): DbAdResponse {
        return safeTransaction({
            val realOwnerId = UsersTable.insertIgnore {
                if (item.ownerId != OwnerIdModel.NONE) {
                    it[id] = item.ownerId.asUUID()
                }
                it[name] = item.ownerId.asUUID().toString()
            } get UsersTable.id

            val res = AdsTable.insert {
                if (item.id != AdIdModel.NONE) {
                    it[id] = item.id.asUUID()
                }
                it[title] = item.title
                it[description] = item.description
                it[ownerId] = realOwnerId
                it[visibility] = item.visibility
                it[dealSide] = item.dealSide
            }

            DbAdResponse(AdsTable.from(res), true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(CommonErrorModel(message = localizedMessage))
            )
        })
    }

    override suspend fun create(req: DbAdModelRequest): DbAdResponse {
        return save(req.ad)
    }

    override suspend fun read(req: DbAdIdRequest): DbAdResponse {
        return safeTransaction({
            val result = (AdsTable innerJoin UsersTable).select { AdsTable.id.eq(req.id.asUUID()) }.single()

            DbAdResponse(AdsTable.from(result), true)
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
            UsersTable.insertIgnore {
                if (ad.ownerId != OwnerIdModel.NONE) {
                    it[id] = ad.ownerId.asUUID()
                }
                it[name] = ad.ownerId.asUUID().toString()
            }
            UsersTable.update({ UsersTable.id.eq(ad.ownerId.asUUID()) }) {
                it[name] = ad.ownerId.asUUID().toString()
            }

            AdsTable.update({ AdsTable.id.eq(ad.id.asUUID()) }) {
                it[title] = ad.title
                it[description] = ad.description
                it[ownerId] = ad.ownerId.asUUID()
                it[visibility] = ad.visibility
                it[dealSide] = ad.dealSide
            }
            val result = AdsTable.select { AdsTable.id.eq(ad.id.asUUID()) }.single()

            DbAdResponse(result = AdsTable.from(result), isSuccess = true)
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
            val result = AdsTable.select { AdsTable.id.eq(req.id.asUUID()) }.single()
            AdsTable.deleteWhere { AdsTable.id eq req.id.asUUID() }

            DbAdResponse(result = AdsTable.from(result), isSuccess = true)
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
            val results = (AdsTable innerJoin UsersTable).select {
                (if (req.ownerId == OwnerIdModel.NONE) Op.TRUE else AdsTable.ownerId eq req.ownerId.asUUID()) and
                        (if (req.dealSide == DealSideModel.NONE) Op.TRUE else AdsTable.dealSide eq req.dealSide)
            }

            DbAdsResponse(result = results.map { AdsTable.from(it) }, isSuccess = true)
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
            return handleException(e)
        }
    }
}
