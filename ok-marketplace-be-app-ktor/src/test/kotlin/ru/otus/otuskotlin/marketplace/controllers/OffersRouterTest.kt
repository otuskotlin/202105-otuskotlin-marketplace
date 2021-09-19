package ru.otus.otuskotlin.marketplace.controllers

import ru.otus.otuskotlin.marketplace.stubs.Bolt
import org.junit.Test
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.Utils.assertListEquals
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdResponse
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OffersRouterTest : RouterTest() {
    @Test
    fun testPostAdCreate() {
        val data = OffersAdRequest(offersAdId = "111", debug = Utils.stubSuccessDebug)

        testPostRequest<OffersAdResponse>(data, "/ad/offers") {
            assertEquals(OffersAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertListEquals(offeredAds!!.map { it.id }, Bolt.getModels().map { it.id.asString() }, false)
        }
    }

}
