package ru.otus.otuskotlin.marketplace.logics.chains.helpers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.validation.IValidationFieldError
import ru.otus.otuskotlin.marketplace.validation.cor.ValidationBuilder
import ru.otus.otuskotlin.marketplace.validation.cor.workers.validation

fun ICorChainDsl<MpContext>.mpValidation(block: ValidationBuilder<MpContext>.() -> Unit) = validation {
//    this.on { status == CorStatus.RUNNING }
//    this.except { status = CorStatus.FAILING }
    errorHandler { validationResult ->
        if (validationResult.isSuccess) return@errorHandler
        val errs = validationResult.errors.map {
            CommonErrorModel(
                message = it.message,
                field = if (it is IValidationFieldError) it.field else "",
            )
        }
        errors.addAll(errs)
        status = CorStatus.FAILING
    }
    block()
}
