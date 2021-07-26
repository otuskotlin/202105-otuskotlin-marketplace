package ru.otus.otuskotlin.marketplace.backend.common.models

data class PaginatedModel(
    val size: Int = Int.MIN_VALUE,
    val lastId: AdIdModel = AdIdModel.NONE,
    val position: PositionModel = PositionModel.NONE,
) {
    companion object {
        val NONE = PaginatedModel()
    }

    enum class PositionModel {
        NONE,
        FIRST,
        MIDDLE,
        LAST;
    }
}
