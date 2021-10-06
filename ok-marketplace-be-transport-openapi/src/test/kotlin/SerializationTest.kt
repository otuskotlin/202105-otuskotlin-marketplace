import com.fasterxml.jackson.databind.ObjectMapper
import ru.otus.otuskotlin.marketplace.openapi.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {
    private val requestId = "11111111-1111-1111-1111-111111111111"
    private val createRequest = CreateAdRequest(
        requestId = requestId,
        createAd = CreateableAd(
            title = "Ad Title",
            description = "Ad Description",
            ownerId = "234",
            visibility = AdVisibility.REGISTERED_ONLY,
            dealSide = AdDealSide.DEMAND,
        )
    )
    private val om = ObjectMapper()

    @Test
    fun serializationTest() {
        val json = om.writeValueAsString(createRequest)
        println(json)
        assertTrue("json must contain discriminator") {
            json.contains(""""messageType":"${createRequest::class.simpleName}"""")
        }
        assertTrue("json must serialize visibility field") {
            json.contains(""""visibility":"${AdVisibility.REGISTERED_ONLY.value}"""")
        }
        assertTrue("json must serialize messageId field") {
            json.contains(""""requestId":"$requestId"""")
        }
    }

    @Test
    fun deserializeTest() {
        val json = om.writeValueAsString(createRequest)
        val deserialized = om.readValue(json, BaseMessage::class.java) as CreateAdRequest

        assertEquals(AdVisibility.REGISTERED_ONLY, deserialized.createAd?.visibility)
        assertEquals(requestId, deserialized.requestId)
    }
}
