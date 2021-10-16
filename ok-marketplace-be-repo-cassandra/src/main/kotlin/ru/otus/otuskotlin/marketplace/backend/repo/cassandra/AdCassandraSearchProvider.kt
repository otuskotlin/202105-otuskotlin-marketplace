package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import ru.otus.otuskotlin.marketplace.backend.common.models.DealSideModel
import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class AdCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<AdCassandraDTO>
) {
    fun search(filter: DbAdFilterRequest): CompletionStage<Collection<AdCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.searchStr.isNotBlank()) {
            select = select
                .whereColumn(AdCassandraDTO.COLUMN_TITLE)
                .like(QueryBuilder.literal("%${filter.searchStr}%"))
        }
        if (filter.ownerId != OwnerIdModel.NONE) {
            select = select
                .whereColumn(AdCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.dealSide != DealSideModel.NONE) {
            select = select
                .whereColumn(AdCassandraDTO.COLUMN_DEAL_SIDE)
                .isEqualTo(QueryBuilder.literal(filter.dealSide, context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<AdCassandraDTO>()
        private val future = CompletableFuture<Collection<AdCassandraDTO>>()
        val stage: CompletionStage<Collection<AdCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}