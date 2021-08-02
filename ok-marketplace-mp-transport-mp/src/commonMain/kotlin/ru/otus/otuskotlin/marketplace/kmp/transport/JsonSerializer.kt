package ru.otus.otuskotlin.marketplace.kmp.transport

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.otus.otuskotlin.marketplace.kmp.transport.models.*

val jsonSerializer = Json {
    prettyPrint = true
    useAlternativeNames = true
    classDiscriminator = "messageType"
    serializersModule = SerializersModule {
        polymorphic(BaseMessage::class) {
            subclass(InitAdRequest::class, InitAdRequest.serializer())
            subclass(CreateAdRequest::class, CreateAdRequest.serializer())
            subclass(ReadAdRequest::class, ReadAdRequest.serializer())
            subclass(UpdateAdRequest::class, UpdateAdRequest.serializer())
            subclass(DeleteAdRequest::class, DeleteAdRequest.serializer())
            subclass(OffersAdRequest::class, OffersAdRequest.serializer())
            subclass(SearchAdRequest::class, SearchAdRequest.serializer())
            subclass(InitAdResponse::class, InitAdResponse.serializer())
            subclass(CreateAdResponse::class, CreateAdResponse.serializer())
            subclass(ReadAdResponse::class, ReadAdResponse.serializer())
            subclass(UpdateAdResponse::class, UpdateAdResponse.serializer())
            subclass(DeleteAdResponse::class, DeleteAdResponse.serializer())
            subclass(OffersAdResponse::class, OffersAdResponse.serializer())
            subclass(SearchAdResponse::class, SearchAdResponse.serializer())
        }
        polymorphic(AdProduct::class) {
            subclass(AdProductBolt::class, AdProductBolt.serializer())
        }
    }
}
