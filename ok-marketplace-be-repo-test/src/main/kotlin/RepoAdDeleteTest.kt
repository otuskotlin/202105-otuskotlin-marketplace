package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import kotlin.test.assertEquals


abstract class RepoAdDeleteTest {
    abstract val repo: IRepoAd

    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.delete(DbAdIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(deleteSuccessStub, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() {
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
            createInitTestModel("delete")
        )
        private val deleteSuccessStub = initObjects.first()
        val successId = deleteSuccessStub.id
        val notFoundId = AdIdModel("ad-repo-delete-notFound")
    }
}
