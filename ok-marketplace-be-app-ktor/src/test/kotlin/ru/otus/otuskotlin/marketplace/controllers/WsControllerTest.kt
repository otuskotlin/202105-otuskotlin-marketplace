package ru.otus.otuskotlin.marketplace.controllers

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import io.ktor.server.testing.withTestApplication
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.module
import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdResponse
import ru.otus.otuskotlin.marketplace.openapi.models.InitAdResponse
import kotlin.test.Test
import kotlin.test.assertIs

class WsControllerTest {
    @Test
    fun test() {
        withTestApplication({ module() }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                run {
                    val responseFrame = incoming.receive()
                    val response = Utils.mapper.readValue<BaseMessage>(responseFrame.readBytes())
                    assertIs<InitAdResponse>(response)
                }

                run {
                    val request = CreateAdRequest(
                        createAd = Utils.stubCreatableAd,
                        debug = Utils.stubSuccessDebug
                    )
                    val requestFrame = Frame.Text(Utils.mapper.writeValueAsString(request))
                    outgoing.send(requestFrame)

                    val responseFrame = incoming.receive()
                    val response = Utils.mapper.readValue<BaseMessage>(responseFrame.readBytes())
                    assertIs<CreateAdResponse>(response)
                }
            }
        }
    }
}
