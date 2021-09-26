package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import kotlin.test.assertEquals


abstract class RepoAdSearchTest {
    abstract val repo: IRepoAd

    @Test
    fun searchSuccess() {
        val result = runBlocking { repo.search(DbAdFilterRequest(ownerId = searchOwnerId)) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[1], initObjects[3])
        assertEquals(expected, result.result.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitAds("search") {

        val searchOwnerId = OwnerIdModel("owner-124")
        override val initObjects: List<AdModel> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3"),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5"),
        )
    }
}
