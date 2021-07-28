package ru.otus.otuskotlin.marketplace.backend.common.models

data class PaginatedModel(
    var size: Int = Int.MIN_VALUE,
    var lastId: AdIdModel = AdIdModel.NONE,
    var position: PositionModel = PositionModel.NONE,
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
