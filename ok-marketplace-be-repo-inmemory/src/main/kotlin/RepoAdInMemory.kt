package ru.otus.otuskotlin.marketplace.backend.repo.inmemory

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.*
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.models.AdRow
import java.time.Duration
import java.util.*


class RepoAdInMemory(
    private val initObjects: List<AdModel> = listOf(),
    private val ttl: Duration = Duration.ofMinutes(1)
) : IRepoAd {

    private val cache: Cache<String, AdRow> = let {
        val cacheManager: CacheManager = CacheManagerBuilder
            .newCacheManagerBuilder()
            .build(true)

        cacheManager.createCache(
            "marketplace-ad-cache",
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                    String::class.java,
                    AdRow::class.java,
                    ResourcePoolsBuilder.heap(100)
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
                .build()
        )
    }

    init {
        runBlocking { initObjects.forEach {
            save(it)
        } }
    }

    private suspend fun save(item: AdModel): DbAdResponse {
        val row = AdRow(item)
        if (row.id == null) {
            return DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        field = "id",
                        message = "Id must not be null or blank"
                    )
                )
            )
        }
        cache.put(row.id, row)
        return DbAdResponse(
            result = row.toInternal(),
            isSuccess = true
        )
    }

    override suspend fun create(req: DbAdModelRequest): DbAdResponse =
        save(req.ad.copy(id = AdIdModel(UUID.randomUUID().toString())))

    override suspend fun read(req: DbAdIdRequest): DbAdResponse = cache.get(req.id.asString())?.let {
        DbAdResponse(
            result = it.toInternal(),
            isSuccess = true
        )
    } ?: DbAdResponse(
        result = null,
        isSuccess = false,
        errors = listOf(
            CommonErrorModel(
                field = "id",
                message = "Not Found"
            )
        )
    )

    override suspend fun update(req: DbAdModelRequest): DbAdResponse {
        val key = req.ad.id.takeIf { it != AdIdModel.NONE }?.asString()
            ?: return DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(field = "id", message = "Id must not be null or blank")
                )
            )

        return if (cache.containsKey(key)) {
            save(req.ad)
            DbAdResponse(
                result = req.ad,
                isSuccess = true
            )
        } else {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        field = "id",
                        message = "Not Found"
                    )
                )
            )
        }
    }

    override suspend fun delete(req: DbAdIdRequest): DbAdResponse {
        val key = req.id.takeIf { it != AdIdModel.NONE }?.asString()
            ?: return DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(field = "id", message = "Id must not be null or blank")
                )
            )
        val row = cache.get(key) ?: return DbAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                CommonErrorModel(field = "id", message = "Not Found")
            )
        )
        cache.remove(key)
        return DbAdResponse(
            result = row.toInternal(),
            isSuccess = true,
        )
    }

    override suspend fun search(req: DbAdFilterRequest): DbAdsResponse {
        val results = cache.asFlow()
            .filter {
                if (req.ownerId == OwnerIdModel.NONE) return@filter true
                req.ownerId.asString() == it.value.ownerId
            }
            .filter {
                if (req.dealSide == DealSideModel.NONE) return@filter true
                req.dealSide.name == it.value.dealSide
            }
            .map { it.value.toInternal() }
            .toList()
        return DbAdsResponse(
            result = results,
            isSuccess = true
        )
    }
}
