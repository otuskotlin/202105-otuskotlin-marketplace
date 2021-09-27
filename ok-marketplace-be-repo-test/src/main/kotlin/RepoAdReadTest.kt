package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import kotlin.test.assertEquals


abstract class RepoAdReadTest {
    abstract val repo: IRepoAd

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.read(DbAdIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.read(DbAdIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(CommonErrorModel(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object: BaseInitAds("search") {
        override val initObjects: List<AdModel> = listOf(
            createInitTestModel("read")
        )
        private val readSuccessStub = initObjects.first()

        val successId = readSuccessStub.id
        val notFoundId = AdIdModel("ad-repo-read-notFound")

    }
}
