package ru.otus.otuskotlin.marketplace

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.openapi.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object Utils {
    val mapper = jacksonObjectMapper()

    fun <T : List<*>> assertListEquals(expected: T, actual: T, checkOrder: Boolean, message: String? = null) {
        if (checkOrder) {
            assertEquals(expected, actual, message)
        } else {
            assertTrue(
                expected.size == actual.size && expected.containsAll(actual) && actual.containsAll(expected),
                "Expected equal unordered list <$expected>, actual <$actual>."
            )
        }
    }

    val stubDebug = BaseDebugRequest(mode = BaseDebugRequest.Mode.STUB)

    val stubResponseAd = ResponseAd(
        title = Bolt.getModel().title,
        description = Bolt.getModel().description,
        ownerId = Bolt.getModel().ownerId.id,
        visibility = AdVisibility.valueOf(Bolt.getModel().visibility.toString()),
        dealSide = AdDealSide.valueOf(Bolt.getModel().dealSide.toString()),
        id = Bolt.getModel().id.id,
        permissions = Bolt.getModel().permissions.map { AdPermissions.valueOf(it.toString()) }.toSet()
    )

    val stubUpdateableAd = UpdateableAd(
        title = stubResponseAd.title,
        description = stubResponseAd.description,
        ownerId = stubResponseAd.ownerId,
        visibility = stubResponseAd.visibility,
        dealSide = stubResponseAd.dealSide,
        id = stubResponseAd.id,
    )
}
