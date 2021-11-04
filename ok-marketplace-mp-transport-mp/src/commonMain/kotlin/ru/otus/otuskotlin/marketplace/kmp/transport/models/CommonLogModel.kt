package ru.otus.otuskotlin.marketplace.kmp.transport.models

/**
 *  General log model
 */
data class CommonLogModel(
    val messageId: String? = null,
    val messageTime: String? = null,
    val logId: String? = null,
    val source: String? = null,
    val marketplace: MpLogModel? = null,
    // поля для других сервисов
    val errors: List<RequestError>? = null,
)
