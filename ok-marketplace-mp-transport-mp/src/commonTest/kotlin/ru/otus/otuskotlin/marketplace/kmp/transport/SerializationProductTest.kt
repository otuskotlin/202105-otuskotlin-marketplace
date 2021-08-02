package ru.otus.otuskotlin.marketplace.kmp.transport

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.otus.otuskotlin.marketplace.kmp.transport.models.AdProductBolt
import ru.otus.otuskotlin.marketplace.kmp.transport.models.BaseMessage
import ru.otus.otuskotlin.marketplace.kmp.transport.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.kmp.transport.models.CreateableAd
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationProductTest {
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
        val serializedString = jsonSerializer.encodeToString(CreateAdRequest.serializer(), dto)
        assertContains(serializedString, Regex("productType\":\\s*\"${AdProductBolt::class.simpleName}"))
    }

    @Test
    fun productDeserializationTest() {
        val serializedString = jsonSerializer.encodeToString(dto)
        val deserializedDto = jsonSerializer.decodeFromString<BaseMessage>(serializedString)
        assertEquals(32.0, ((deserializedDto as CreateAdRequest).createAd?.product as AdProductBolt).lengh)
    }
}
