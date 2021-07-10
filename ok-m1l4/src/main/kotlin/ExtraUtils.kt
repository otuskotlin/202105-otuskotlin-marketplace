// Простое переименование типа, в данном случае EmailType абсолютно тоже самое что и String
typealias EmailType = String

val aEmail: EmailType = "email"
val aString: String = "email"

// Класс обертка, в котором есть функциональность и возможность контролировать значение
@JvmInline
value class Email private constructor(val email: String) {
    init {
        if (!email.contains("@")) {
            throw IllegalArgumentException("Wrong email $email")
        }
    }

    companion object {
        fun fromGmail(email: String): Email {
            return Email("$email@gmail.com")
        }
    }
}


fun main() {
    val b = Email.fromGmail("sample@email.com")
}


data class SimpleUser(
    val name: String,
    val age: String,
)