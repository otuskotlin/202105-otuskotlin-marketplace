package ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.kmp.transport.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MappingTest {

    @Test
    fun setUpdateQueryMappingTest() {
        val query = UpdateAdRequest(
            requestId = "12345",
            createAd = UpdateableAd(
                id = "id-1",
                title = "title-1",
                description = "description-1",
                ownerId = "owner_id-1",
                visibility = AdVisibility.REGISTERED_ONLY,
                dealSide = AdDealSide.DEMAND,
            )
        )
        val context = MpContext().setQuery(query)
        assertEquals("12345", context.onRequest)
        assertEquals("id-1", context.requestAd.id.asString())
        assertEquals("title-1", context.requestAd.title)
        assertEquals("description-1", context.requestAd.description)
        assertEquals("owner_id-1", context.requestAd.ownerId.asString())
        assertEquals(AdVisibilityModel.REGISTERED_ONLY, context.requestAd.visibility)
        assertEquals(DealSideModel.DEMAND, context.requestAd.dealSide)
    }

    @Test
    fun updateResponseMappingTest() {
        val context = MpContext(
            onRequest = "12345",
            responseAd = AdModel(
                id = AdIdModel("id-1"),
                title = "title-1",
                description = "description-1",
                ownerId = OwnerIdModel("owner_id-1"),
                visibility = AdVisibilityModel.REGISTERED_ONLY,
                dealSide = DealSideModel.DEMAND,
            ),
            errors = mutableListOf(CommonErrorModel(level = IError.Level.WARNING)),
        )
        val response = context.toUpdateResponse()
        assertEquals("12345", response.requestId)
        assertEquals("id-1", response.updatedAd?.id)
        assertEquals("title-1", response.updatedAd?.title)
        assertEquals("description-1", response.updatedAd?.description)
        assertEquals("owner_id-1", response.updatedAd?.ownerId)
        assertEquals(AdVisibility.REGISTERED_ONLY, response.updatedAd?.visibility)
        assertEquals(AdDealSide.DEMAND, response.updatedAd?.dealSide)
        assertEquals(BaseResponse.Result.SUCCESS, response.result)
        assertEquals(1, response.errors?.size)
    }
}
