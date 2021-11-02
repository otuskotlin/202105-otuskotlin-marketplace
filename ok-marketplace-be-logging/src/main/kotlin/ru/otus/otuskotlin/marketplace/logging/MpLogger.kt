package ru.otus.otuskotlin.marketplace.logging

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import java.time.Instant

suspend fun <T> Logger.wrapWithLogging(
    id: String = "",
    block: suspend () -> T,
): T = try {
    val timeStart = Instant.now()
    info("Entering $id")
    block().also {
        val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
        info("Finishing $id", Pair("metricHandleTime", diffTime))
    }
} catch (e: Throwable) {
    error("Failing $id", e)
    throw e
}

fun mpLogger(loggerId: String): MpLogWrapper = mpLogger(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

fun mpLogger(cls: Class<out Any>): MpLogWrapper = mpLogger(
    logger = LoggerFactory.getLogger(cls) as Logger
)

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mpLogger(logger: Logger): MpLogWrapper = MpLogWrapper(
    logger = logger,
    loggerId = logger.name,
)
