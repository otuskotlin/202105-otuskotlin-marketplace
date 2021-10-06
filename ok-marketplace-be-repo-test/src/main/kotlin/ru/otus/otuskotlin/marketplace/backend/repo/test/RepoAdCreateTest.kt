package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


abstract class RepoAdCreateTest {
    abstract val repo: IRepoAd

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.create(DbAdModelRequest(createObj)) }
        val expected = createObj.copy(id = result.result?.id ?: AdIdModel.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertNotEquals(AdIdModel.NONE, result.result?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitAds() {

        private val createObj = AdModel(
            title = "create object",
            description = "create object description",
            ownerId = OwnerIdModel(UUID.randomUUID()),
            visibility = AdVisibilityModel.REGISTERED_ONLY,
            dealSide = DealSideModel.PROPOSAL,
        )
        override val initObjects: List<AdModel> = emptyList()
    }
}
