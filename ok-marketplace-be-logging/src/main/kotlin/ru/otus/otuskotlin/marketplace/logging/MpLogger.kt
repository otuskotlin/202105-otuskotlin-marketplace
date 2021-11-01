package ru.otus.otuskotlin.marketplace.logging

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

fun mpLogger(loggerId: String): MpLogContext = mpLogger(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

fun mpLogger(cls: Class<out Any>): MpLogContext = mpLogger(
    logger = LoggerFactory.getLogger(cls) as Logger
)

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mpLogger(logger: Logger): MpLogContext = MpLogContext(
    logger = logger,
    loggerId = logger.name,
)
