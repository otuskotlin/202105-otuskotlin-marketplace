package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.logging.MpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.toModel
import java.time.Instant

suspend inline fun <reified Request : Any, reified Response : Any> ApplicationCall.handleRequest(
    logId: String,
    logger: MpLogWrapper,
    crossinline block: suspend MpContext.(Request) -> Response,
    crossinline except: suspend MpContext.(Throwable) -> Response
) {
    logger.doWithLogging(logId) {
        val context = MpContext(startTime = Instant.now(), principal = principal<JWTPrincipal>().toModel())

        val res = try {
            val req = receive<Request>()
            context.block(req)
        } catch (e: Throwable) {
            withContext(NonCancellable) {
                context.except(e)
            }
        }
        respond(res)
    }
}
