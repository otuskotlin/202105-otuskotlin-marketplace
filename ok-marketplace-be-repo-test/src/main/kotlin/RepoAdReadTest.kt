package ru.otus.otuskotlin.marketplace.backend.repo.test

import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd


abstract class RepoAdReadTest {
    abstract val repo: IRepoAd

    @Test
    fun success() {
        val ctx = MpContext(
            requestAdId = AdIdModel(successId)
        )

        runBlocking {}

    }

    companion object {
        const val successId = "ad-repo-read-success"
        const val notFoundId = "ad-repo-read-notFound"

    }
}
