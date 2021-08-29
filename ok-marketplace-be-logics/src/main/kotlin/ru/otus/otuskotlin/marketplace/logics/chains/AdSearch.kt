package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.AdSearchStub
import ru.otus.otuskotlin.marketplace.logics.workers.AnswerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.ChainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.CheckOperationWorker

object AdSearch: ICorExec<MpContext> by chain<MpContext>({
    // Проверка, что операция соответствует выбранному чейну
    add(CheckOperationWorker(MpContext.MpOperations.SEARCH))
    // Инициализация статуса чейна
    add(ChainInitWorker)
    // TODO: Валидация запроса

    // Обработка стаба
    add(AdSearchStub)

    // TODO: продовая логика, работа с БД

    // Подготовка ответа
    add(AnswerPrepareChain)
}).build()
