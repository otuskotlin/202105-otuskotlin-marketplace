package ru.otus.otuskotlin.marketplace.backend.services.exceptions

import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage

class DataNotAllowedException(msg: String, request: BaseMessage) : Throwable("$msg: $request")
