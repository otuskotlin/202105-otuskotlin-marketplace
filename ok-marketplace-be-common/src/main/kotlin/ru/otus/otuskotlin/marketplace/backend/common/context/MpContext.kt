package ru.otus.otuskotlin.marketplace.backend.common.context

import ru.otus.otuskotlin.marketplace.backend.common.models.*
import java.time.Instant

data class MpContext(
    var startTime : Instant = Instant.MIN,
    var operation: MpOperations = MpOperations.NONE,
    var stubCase: MpStubCase = MpStubCase.NONE,

    val userSession: IUserSession<*> = EmptySession,

    var onRequest: String = "",
    var requestAdId: AdIdModel = AdIdModel.NONE,
    var requestAd: AdModel = AdModel(),
    var responseAd: AdModel = AdModel(),
    var requestPage: PaginatedModel = PaginatedModel(),
    var responsePage: PaginatedModel = PaginatedModel(),
    var responseAds: MutableList<AdModel> = mutableListOf(),
    val errors: MutableList<IError> = mutableListOf(),
    var status: CorStatus = CorStatus.NONE,
) {
    enum class MpOperations {
        NONE,
        INIT,
        CREATE,
        READ,
        UPDATE,
        DELETE,
        SEARCH,
        OFFER
    }

    /**
     * Добавляет ошибку в контекст
     *
     * @param error Ошибка, которую необходимо добавить в контекст
     * @param failingStatus Необходимо ли установить статус выполнения в FAILING (true/false)
     */
    fun addError(error: IError, failingStatus: Boolean = true) = apply {
        if (failingStatus) status = CorStatus.FAILING
        errors.add(error)
    }


    fun addError(
        e: Throwable,
        level: IError.Level = IError.Level.ERROR,
        field: String = "",
        failingStatus: Boolean = true
    ) {
        addError(CommonErrorModel(e, field = field, level = level), failingStatus)
    }
}
