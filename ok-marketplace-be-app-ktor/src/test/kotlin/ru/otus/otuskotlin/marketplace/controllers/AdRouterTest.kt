package ru.otus.otuskotlin.marketplace.controllers

import marketplace.stubs.Bolt
import org.junit.Test
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.Utils.assertListEquals
import ru.otus.otuskotlin.marketplace.Utils.stubUpdateableAd
import ru.otus.otuskotlin.marketplace.openapi.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdRouterTest : RouterTest() {
    @Test
    fun testPostAdCreate() {
        val data = CreateAdRequest(debug = Utils.stubDebug)

        testPostRequest<CreateAdResponse>(data, "/ad/create") {
            assertEquals(CreateAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponseAd, createdAd)
        }
    }

    @Test
    fun testPostAdRead() {
        val data = ReadAdRequest(readAdId = "99999", debug = Utils.stubDebug)

        testPostRequest<ReadAdResponse>(data, "/ad/read") {
            assertEquals(ReadAdResponse.Result.ERROR, result)
            assertNotNull(errors)
            errors!!.apply {
                assertTrue(isNotEmpty())
                assertNotNull(find {
                    it.field == "requestedAdId" && (it.message?.contains("99999") ?: false)
                })
            }
        }

        testPostRequest<ReadAdResponse>(data.copy(readAdId = "666"), "/ad/read") {
            assertEquals(ReadAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponseAd, readAd)
        }
    }

    @Test
    fun testPostAdUpdate() {
        val data = UpdateAdRequest(createAd = stubUpdateableAd, debug = Utils.stubDebug)

        testPostRequest<UpdateAdResponse>(data, "/ad/update") {
            assertEquals(UpdateAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            // permissions must be null according to stubs
            assertEquals(Utils.stubResponseAd.copy(permissions = null), updatedAd)
        }
    }

    @Test
    fun testPostAdDelete() {
        val data = DeleteAdRequest(deleteAdId = "98765", debug = Utils.stubDebug)

        testPostRequest<DeleteAdResponse>(data, "/ad/delete") {
            assertEquals(DeleteAdResponse.Result.SUCCESS, result)
            assertNotNull(errors)
            errors!!.apply {
                assertTrue(isNotEmpty())
                assertNotNull(find { it.field == "id" })
            }
        }

        testPostRequest<DeleteAdResponse>(data.copy(deleteAdId = "666"), "/ad/delete") {
            assertEquals(DeleteAdResponse.Result.SUCCESS, result)
            assertNull(errors)
        }
    }

    @Test
    fun testPostAdSearch() {
        val data = SearchAdRequest(requestId = "99", page = BasePaginatedRequest(size = 3, lastId = "666"))

        testPostRequest<SearchAdResponse>(data, "/ad/search") {
            assertEquals(SearchAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertNotNull(foundAds)
            assertListEquals(foundAds!!.map { it.id }, Bolt.getModels().map { it.id.id }, false)
        }
    }
}