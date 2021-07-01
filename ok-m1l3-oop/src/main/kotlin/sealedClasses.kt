package ru.otus.otuskotlin.oop

sealed interface Base

class ChildA : Base

class ChildB : Base

// Uncomment this to get compilation error
//class ChildC : Base

fun main() {
    val obj: Base = ChildA()

    val result = when (obj) {
        is ChildA -> "a"
        is ChildB -> "b"
    }

    println(result)
}