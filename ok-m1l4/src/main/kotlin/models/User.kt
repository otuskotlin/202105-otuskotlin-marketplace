package models

import java.time.LocalDate
import java.util.*

enum class Action {
    DELETE,
    READ,
    WRITE,
    UPDATE,
    CREATE
}

data class User(
    val id: UserId,

    val firstName: String,
    val secondName: String,
    val lastName: String,

    val birthDate: LocalDate,
    val email: Email,
    val phone: Phone,

    val actions: Set<Action>,
)

@JvmInline
value class UserId(val value: String) {
    companion object {
        val NONE = UserId("")
        fun random() = UserId(UUID.randomUUID().toString())
    }
}

@JvmInline
value class Email(val value: String) {
    companion object {
        val NONE = Email("")
    }
}

@JvmInline
value class Phone(val value: String) {
    companion object {
        val NONE = Phone("")
    }
}
