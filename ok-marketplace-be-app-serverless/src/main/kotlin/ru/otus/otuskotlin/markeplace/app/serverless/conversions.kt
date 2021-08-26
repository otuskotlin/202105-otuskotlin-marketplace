package ru.otus.otuskotlin.markeplace.app.serverless

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage
import java.util.*

val objectMapper = jacksonObjectMapper().findAndRegisterModules()

inline fun <reified T> APIGatewayV2HTTPEvent.toTransportModel(): T =
    if (isBase64Encoded) {
        objectMapper.readValue(Base64.getDecoder().decode(body))
    } else {
        objectMapper.readValue(body)
    }

fun BaseMessage.toAPIGatewayV2HTTPResponse(): APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withHeaders(mapOf("Content-Type" to "application/json"))
        .withBody(objectMapper.writeValueAsString(this))
        .build()