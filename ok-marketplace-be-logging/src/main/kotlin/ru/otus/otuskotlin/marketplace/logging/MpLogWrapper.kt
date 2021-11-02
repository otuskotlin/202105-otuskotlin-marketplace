package ru.otus.otuskotlin.marketplace.logging

import ch.qos.logback.classic.Logger
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent
import java.time.Instant

class MpLogWrapper(val logger: Logger, val loggerId: String = "") {
    fun log(
        msg: String = "",
        level: Level = Level.TRACE,
        e: Throwable? = null,
        data: Any? = null,
        vararg objs: Pair<String, Any>?
    ) {
        logger.log(object : LoggingEvent {
            override fun getThrowable() = e
            override fun getTimeStamp(): Long = Instant.now().toEpochMilli()
            override fun getThreadName(): String = Thread.currentThread().name
            override fun getMessage(): String = msg
            override fun getMarker(): Marker? = null
            override fun getArgumentArray(): Array<out Any> = data?.let { d ->
                arrayOf(
                    *objs.map { StructuredArguments.keyValue(it?.first, it?.second) }.toTypedArray(),
                    StructuredArguments.keyValue("data", d),
                ).filterNotNull().toTypedArray()
            } ?: objs.mapNotNull { StructuredArguments.keyValue(it?.first, it?.second) }.toTypedArray()

            override fun getLevel(): Level = level
            override fun getLoggerName(): String = logger.name
        })
    }
    suspend fun <T> doWithLogging(
        id: String = "",
        level: Level = Level.INFO,
        block: suspend () -> T,
    ): T = try {
        val timeStart = Instant.now()
        log("$loggerId Entering $id", level)
        block().also {
            val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
            log(
                "$loggerId Finishing $id", level,
                objs = arrayOf(Pair("metricHandleTime", diffTime))
            )
        }
    } catch (e: Throwable) {
        log("$loggerId Failing $id", Level.ERROR, e)
        throw e
    }

    suspend fun <T> doWithErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (e: Throwable) {
        log("$loggerId Failing $id", Level.ERROR, e)
        if (throwRequired) throw e else null
    }
}
