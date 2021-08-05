package ru.otus.otuskotlin.marketplace.kmp.transport.models
import kotlinx.serialization.Serializable

/**
 * Описание продукта Macbook
 *
 * @param osVersion Версия ОС
 * @param storage Объем накопителя
 * @param model Модель макбука
 * @param display Размер дисплея
 */

@Serializable
data class AdProductMacbook(
    val osVersion: String?,
    val storage: String?,
    val model: String,
    val display: Double
): AdProduct  {
    override val productType: String = this::class.simpleName!!
}