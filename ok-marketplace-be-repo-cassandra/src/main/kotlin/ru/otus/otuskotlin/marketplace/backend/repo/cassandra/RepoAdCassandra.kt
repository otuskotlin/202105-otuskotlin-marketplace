package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import kotlinx.coroutines.future.await
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdResponse
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdsResponse
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import java.util.*

class RepoAdCassandra(
    private val dao: AdCassandraDAO,
    private val timeoutMillis: Long = 30_000
) : IRepoAd {
    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun create(req: DbAdModelRequest): DbAdResponse {
        val new = req.ad.copy(id = AdIdModel(UUID.randomUUID().toString()))
        return try {
            withTimeout(timeoutMillis) { dao.create(AdCassandraDTO(new)).await() }
            DbAdResponse(new)
        } catch (e: Exception) {
            log.error("Failed to create", e)
            DbAdResponse(CommonErrorModel(e))
        }
    }

    override suspend fun read(req: DbAdIdRequest): DbAdResponse {
        return if (req.id == AdIdModel.NONE)
            DbAdResponse(CommonErrorModel(field = "id", message = "Id is empty"))
        else try {
            val found = withTimeout(timeoutMillis) { dao.read(req.id.asString()).await() }
            if (found != null) DbAdResponse(found.toAdModel())
            else DbAdResponse(CommonErrorModel(field = "id", message = "Not Found"))
        } catch (e: Exception) {
            log.error("Failed to read", e)
            DbAdResponse(CommonErrorModel(e))
        }
    }

    override suspend fun update(req: DbAdModelRequest): DbAdResponse {
        return if (req.ad.id == AdIdModel.NONE)
            DbAdResponse(CommonErrorModel(field = "ad.id", message = "Id is empty"))
        else try {
            val updated = withTimeout(timeoutMillis) {
                dao.update(AdCassandraDTO(req.ad)).await()
                dao.read(req.ad.id.asString()).await()
            }
            if (updated != null) DbAdResponse(updated.toAdModel())
            else DbAdResponse(CommonErrorModel(field = "id", message = "Not Found"))
        } catch (e: Exception) {
            log.error("Failed to update", e)
            DbAdResponse(CommonErrorModel(e))
        }
    }

    override suspend fun delete(req: DbAdIdRequest): DbAdResponse {
        return if (req.id == AdIdModel.NONE)
            DbAdResponse(CommonErrorModel(field = "id", message = "Id is empty"))
        else try {
            val deleted = withTimeout(timeoutMillis) {
                dao.read(req.id.asString()).await()
                    ?.also { dao.delete(it).await() }
            }
            if (deleted != null) DbAdResponse(deleted.toAdModel())
            else DbAdResponse(CommonErrorModel(field = "id", message = "Not Found"))
        } catch (e: Exception) {
            log.error("Failed to delete", e)
            DbAdResponse(CommonErrorModel(e))
        }
    }


    override suspend fun search(req: DbAdFilterRequest): DbAdsResponse {
        return try {
            val found = withTimeout(timeoutMillis) { dao.search(req) }.await()
            DbAdsResponse(found.map { it.toAdModel() })
        } catch (e: Exception) {
            log.error("Failed to search", e)
            DbAdsResponse(CommonErrorModel(e))
        }
    }
}