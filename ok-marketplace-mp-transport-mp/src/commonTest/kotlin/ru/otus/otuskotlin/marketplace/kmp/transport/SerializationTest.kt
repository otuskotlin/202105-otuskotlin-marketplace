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
        val dto = CreateAdRequest(
            requestId = "12345"
        )
        val serializedString = jsonSerializer.encodeToString(CreateAdRequest.serializer(), dto)
        assertContains(serializedString, Regex("requestId\":\\s*\"12345"))
    }

    @Test
    fun adSerializePolymorphicTest() {
        val dto: BaseMessage = CreateAdRequest(
            requestId = "12345"
        )
        val serializedString = jsonSerializer.encodeToString(dto)
        println(serializedString)
        assertContains(serializedString, Regex("requestId\":\\s*\"12345"))
        assertContains(serializedString, Regex("messageType\":\\s*\"${CreateAdRequest::class.simpleName}"))
        val deserializedDto = jsonSerializer.decodeFromString<BaseMessage>(serializedString)
        assertEquals("12345", (deserializedDto as CreateAdRequest).requestId)
    }
}
