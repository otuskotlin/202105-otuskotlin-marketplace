import com.fasterxml.jackson.databind.ObjectMapper
import ru.otus.otuskotlin.marketplace.openapi.models.AdProductBolt
import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.CreateableAd
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationProductTest {
    private val jsonSerializer = ObjectMapper()
    val dto = CreateAdRequest(
        requestId = "12345",
        createAd = CreateableAd(
            title = "Bolt",
            description = "Strong Bolt",
            product = AdProductBolt(
                lengh = 32.0,
                diameter = 8.0,
                threadPitch = 0.6,
            )
        )
    )

    @Test
    fun productDiscriminatorTest() {
        val serializedString = jsonSerializer.writeValueAsString(dto)
        assertContains(serializedString, Regex("productType\":\\s*\"${AdProductBolt::class.simpleName}"))
    }

    @Test
    fun productDeserializationTest() {
        val serializedString = jsonSerializer.writeValueAsString(dto)
        val deserializedDto = jsonSerializer.readValue(serializedString, BaseMessage::class.java)
        assertEquals(32.0, ((deserializedDto as CreateAdRequest).createAd?.product as AdProductBolt).lengh)
    }
}
