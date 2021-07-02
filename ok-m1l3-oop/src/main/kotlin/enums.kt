package ru.otus.otuskotlin.oop

enum class MyEnum {
    FOO {
        override fun doSmth() {
            TODO("Not yet implemented")
        }
    },

    BAR {
        override fun doSmth() {
            TODO("Not yet implemented")
        }
    };

    abstract fun doSmth()
}