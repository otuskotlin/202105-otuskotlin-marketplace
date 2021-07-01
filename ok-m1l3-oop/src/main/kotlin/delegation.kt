package ru.otus.otuskotlin.oop

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ConstValue(
    private val value: Int
) : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        TODO("Not yet implemented")
    }
}

class DelegateExample {
    val constVal by ConstValue(100501)
}

fun main() {
    val example = DelegateExample()
    println(example.constVal)
}