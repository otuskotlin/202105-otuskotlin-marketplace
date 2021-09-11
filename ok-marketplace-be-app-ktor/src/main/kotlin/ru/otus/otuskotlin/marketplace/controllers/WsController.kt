package ru.otus.otuskotlin.marketplace.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.IUserSession
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toUpdateResponse
import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.DeleteAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.InitAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.ReadAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.SearchAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.UpdateAdRequest
import java.time.Instant

// Это расширение делает вызов потенциально блокирующего метода writeValueAsString безопасным для корутин
suspend fun ObjectMapper.safeWriteValueAsString(value: Any): String =
    withContext(Dispatchers.IO) { writeValueAsString(value) }

class KtorUserSession(
    override val fwSession: WebSocketSession,
    private val objectMapper: ObjectMapper,
) : IUserSession<WebSocketSession> {
    override suspend fun notifyAdChanged(context: MpContext) {
        val event = context.toUpdateResponse()
        fwSession.send(Frame.Text(objectMapper.safeWriteValueAsString(event)))
    }
}

suspend fun WebSocketSession.handleSession(
    objectMapper: ObjectMapper,
    adService: AdService,
    userSessions: MutableSet<KtorUserSession>
) {
    val userSession = KtorUserSession(this, objectMapper)
    userSessions.add(userSession)

    try {
        run {
            // Обработка события init сессии
            serveRequest(InitAdRequest(), userSession, adService)?.also {
                outgoing.send(Frame.Text(objectMapper.safeWriteValueAsString(it)))
            }
        }

        for (frame in incoming) {
            if (frame is Frame.Text) {
                // Обработка события message сессии
                val request = objectMapper.readValue<BaseMessage>(frame.readText())
                serveRequest(request, userSession, adService)?.also {
                    outgoing.send(Frame.Text(objectMapper.safeWriteValueAsString(it)))
                }
            }
        }
    } catch (_: ClosedReceiveChannelException) {
        // Веб-сокет закрыт по инициативе клиента
    } finally {
        userSessions.remove(userSession)
    }

    // Обработка события finished сессии
    serveRequest(null, userSession, adService)
}

suspend fun serveRequest(request: BaseMessage?, userSession: KtorUserSession, adService: AdService): BaseMessage? {
    val context = MpContext(startTime = Instant.now(), userSession = userSession)
    return try {
        when (request) {
            is InitAdRequest -> adService.initAd(context, request)
            is CreateAdRequest -> adService.createAd(context, request)
            is ReadAdRequest -> adService.readAd(context, request)
            is UpdateAdRequest -> adService.updateAd(context, request)
            is DeleteAdRequest -> adService.deleteAd(context, request)
            is SearchAdRequest -> adService.searchAd(context, request)
            is OffersAdRequest -> adService.offersAd(context, request)
            null -> {
                adService.finishAd(context)
                null
            }
            else -> throw UnsupportedOperationException("Unsupported request type")
        }
    } catch (e: Exception) {
        adService.errorAd(context, e)
    }
}