package ru.otus.otuskotlin.marketplace.backend.common.models

data class CommonErrorModel(
    override var field: String = "",
    override var level: IError.Level = IError.Level.ERROR,
    override var message: String = "",
    override var stackTrace: IError.StackTrace = IError.StackTrace.NONE,
): IError {
    fun from(e: Throwable, level: IError.Level = IError.Level.ERROR) {
        this.level = level
        message = e.message ?: ""
        stackTrace = IError.StackTrace(e.stackTrace)
    }
}
