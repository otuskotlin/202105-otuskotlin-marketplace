package ru.otus.otuskotlin.markeplace.springapp

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.backend.common.models.IError

fun MpContext.addError(lambda: CommonErrorModel.() -> Unit) =
    apply {
        status = CorStatus.FAILING
        errors.add(
            CommonErrorModel(
                field = "_", level = IError.Level.ERROR
            ).apply(lambda)
        )
    }