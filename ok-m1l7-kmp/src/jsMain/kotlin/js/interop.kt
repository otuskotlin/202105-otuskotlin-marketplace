package ru.otus.otuskotlin.marketplace.kmp.js

import org.w3c.dom.Window

external val window: Window

val windowLocation = window.location

fun useMathRound(value: Double) = js("Math.round(value)")

@JsModule("is-sorted")
@JsNonModule
external fun <T> sorted(array: Array<T>): Boolean

@JsModule("js-big-decimal")
@JsNonModule
@JsName("bigDecimal")
external class JsBigDecimal(value: Any) {
    fun getValue(): String
    fun getPrettyValue(digits: Int, separator: String)
    fun round(precision: Int = definedExternally, mode: dynamic = definedExternally): JsBigDecimal
    companion object {
        fun getPrettyValue(number: Any, digits: Int, separator: String)
    }
}

