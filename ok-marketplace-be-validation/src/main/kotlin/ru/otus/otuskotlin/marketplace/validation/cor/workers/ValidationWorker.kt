package ru.otus.otuskotlin.marketplace.validation.cor.workers

import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.validation.cor.ValidationBuilder

fun <C> ICorChainDsl<C>.validation(block: ValidationBuilder<C>.() -> Unit) {
    worker {
        this.title = "Валидация"
        description = "Валидация логики"
//        on { status == CorStatus.RUNNING }
//        except { status = CorStatus.FAILING }
        handle {
            ValidationBuilder<C>().apply(block).build().exec(this)
        }
    }
}