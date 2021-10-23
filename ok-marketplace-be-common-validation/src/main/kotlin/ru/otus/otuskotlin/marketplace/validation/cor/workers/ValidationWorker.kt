package ru.otus.otuskotlin.marketplace.validation.cor.workers

import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.validation.cor.ValidationBuilder

fun <C> ICorChainDsl<C>.validation(block: ValidationBuilder<C>.() -> Unit) = worker {
    this.title = "Валидация"
    description = "Валидация логики"
    val validator = ValidationBuilder<C>().apply(block).build()
    handle {
        validator.exec(this)
    }
}
