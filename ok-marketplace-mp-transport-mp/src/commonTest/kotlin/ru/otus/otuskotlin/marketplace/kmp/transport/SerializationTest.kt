package ru.otus.otuskotlin.marketplace.kmp.transport

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.otus.otuskotlin.marketplace.kmp.transport.models.BaseMessage
import ru.otus.otuskotlin.marketplace.kmp.transport.models.CreateAdRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationTest {

    @Test
    fun adSerializeTest() {
        val json = Json {
            prettyPrint = true
        }
        val dto = CreateAdRequest(
            requestId = "12345"
        )
        val serializedString = json.encodeToString(CreateAdRequest.serializer(), dto)
        assertContains(serializedString, Regex("requestId\":\\s*\"12345"))
    }

    @Test
    fun adSerializePolymorphicTest() {
        val jsonRequest = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(BaseMessage::class) {
                    subclass(CreateAdRequest::class, CreateAdRequest.serializer())
                }

            }
        }
        val dto: BaseMessage = CreateAdRequest(
            requestId = "12345"
        )
        val serializedString = jsonRequest.encodeToString(dto)
        assertContains(serializedString, Regex("requestId\":\\s*\"12345"))
        val deserializedDto = jsonRequest.decodeFromString<BaseMessage>(serializedString)
        assertEquals("12345", (deserializedDto as CreateAdRequest).requestId)
    }
}
