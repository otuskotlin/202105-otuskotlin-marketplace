package ru.otus.otuskotlin.marketplace.backend.common.exceptions

class MpStubCaseNotFound(stubCase: String): Throwable("There is no matchable worker to handle case: $stubCase")
