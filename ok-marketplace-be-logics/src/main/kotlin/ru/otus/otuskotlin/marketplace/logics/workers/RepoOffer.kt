package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.repoOffer(title: String) = chain {
    this.title = title
    description = """
        Search for offers for the specified Ad. The provided in request ID is used to get object from DB.
        Then proper objects are searched in DB among opposite Ads.
    """.trimIndent()

    on { status == CorStatus.RUNNING }

    repoRead("Чтение объекта, для которого предложения")

    worker {
        this.title = "Search offers for the ad"
        description = """
            Search for Ads those are most appropriate to the found one on previous step
        """.trimIndent()

        on { status == CorStatus.RUNNING }

        handle {
            val filter = DbAdFilterRequest(
                dealSide = dbAd.dealSide.opposite()
            )
            val result = adRepo.search(filter)
            if (result.isSuccess) {
                responseAds =  result.result.toMutableList()
            } else {
                result.errors.forEach {
                    addError(it)
                }
            }
        }
    }
}
