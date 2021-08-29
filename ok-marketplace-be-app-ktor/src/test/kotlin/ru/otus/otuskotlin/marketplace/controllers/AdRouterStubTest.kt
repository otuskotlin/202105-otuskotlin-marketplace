package ru.otus.otuskotlin.marketplace.controllers

import ru.otus.otuskotlin.marketplace.stubs.Bolt
import org.junit.Test
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.Utils.assertListEquals
import ru.otus.otuskotlin.marketplace.Utils.stubCreatableAd
import ru.otus.otuskotlin.marketplace.Utils.stubSuccessDebug
import ru.otus.otuskotlin.marketplace.Utils.stubUpdateableAd
import ru.otus.otuskotlin.marketplace.openapi.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AdRouterTest : RouterTest() {
    @Test
    fun testPostAdCreate() {
        val data = CreateAdRequest(createAd = stubCreatableAd, debug = Utils.stubSuccessDebug)

        testPostRequest<CreateAdResponse>(data, "/ad/create") {
            assertEquals(CreateAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponseAd, createdAd)
        }
    }

    @Test
    fun testPostAdRead() {
        val data = ReadAdRequest(readAdId = "99999", debug = Utils.stubSuccessDebug)

        testPostRequest<ReadAdResponse>(data, "/ad/read") {
            assertEquals(ReadAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponseAd.copy(id = "99999"), readAd)
        }
    }

    @Test
    fun testPostAdUpdate() {
        val data = UpdateAdRequest(createAd = stubUpdateableAd, debug = Utils.stubSuccessDebug)

        testPostRequest<UpdateAdResponse>(data, "/ad/update") {
            assertEquals(UpdateAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponseAd, updatedAd)
        }
    }

    @Test
    fun testPostAdDelete() {
        val data = DeleteAdRequest(deleteAdId = "98765", debug = Utils.stubSuccessDebug)

        testPostRequest<DeleteAdResponse>(data, "/ad/delete") {
            assertEquals(DeleteAdResponse.Result.SUCCESS, result)
            assertNull(errors)
        }
    }

    @Test
    fun testPostAdSearch() {
        val data = SearchAdRequest(
            requestId = "99",
            page = BasePaginatedRequest(size = 3, lastId = "666"),
            debug = stubSuccessDebug
        )

        testPostRequest<SearchAdResponse>(data, "/ad/search") {
            assertEquals(SearchAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertNotNull(foundAds)
            assertListEquals(foundAds!!.map { it.id }, Bolt.getModels().map { it.id.id }, false)
        }
    }
}
