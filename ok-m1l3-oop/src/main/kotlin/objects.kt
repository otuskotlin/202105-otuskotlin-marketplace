package ru.otus.otuskotlin.oop

class ObjectsExample {
    companion object {
        fun doSmth() {
            println("companion object")
        }
    }

    object A {
        fun doSmth() {
            println("object A")
        }
    }
}

fun main() {
    ObjectsExample.doSmth()
    ObjectsExample.A.doSmth()
}