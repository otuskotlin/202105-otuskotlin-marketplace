package dsl

import models.*
import java.time.LocalDate

@UserDslMarker
class NameContext {
    var first: String = ""
    var second: String = ""
    var last: String = ""
}

@UserDslMarker
class BirthContext {
    var date: String = ""
}

@UserDslMarker
class ContactsDsl {
    var email: String = ""
    var phone: String = ""
}

class ActionsContext {
    private val _actions = mutableSetOf<Action>()

    val actions
        get() = _actions.toSet()

    fun add(action: Action) {
        _actions.add(action)
    }

    fun add(action: String) = add(Action.valueOf(action))

    operator fun Action.unaryPlus() = add(this)

    operator fun String.unaryPlus() = add(this)
}

@UserDslMarker
class UserBuilder {
    var id: UserId = UserId.random()

    var firstName: String = ""
    var secondName: String = ""
    var lastName: String = ""

    var birthDate: LocalDate = LocalDate.MIN
    var email: Email = Email.NONE
    var phone: Phone = Phone.NONE

    var actions: Set<Action> = mutableSetOf()

    @UserDslMarker
    fun name(block: NameContext.() -> Unit) {
        val context = NameContext().apply(block)
        firstName = context.first
        secondName = context.second
        lastName = context.last
    }

    @UserDslMarker
    fun birth(block: BirthContext.() -> Unit) {
        val context = BirthContext().apply(block)
        birthDate = LocalDate.parse(context.date)
    }

    @UserDslMarker
    fun actions(block: ActionsContext.() -> Unit) {
        val context = ActionsContext().apply(block)
        actions = context.actions
    }

    fun build(): User {
        return User(
            id = id,
            firstName = firstName,
            secondName = secondName,
            lastName = lastName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            actions = actions
        )
    }
}

@UserDslMarker
fun UserBuilder.contacts(block: ContactsDsl.() -> Unit) {
    val context = ContactsDsl().apply(block)
    email = Email(context.email)
    phone = Phone(context.phone)
}

@UserDslMarker
fun user(block: UserBuilder.() -> Unit) = UserBuilder().apply(block).build()

@DslMarker
annotation class UserDslMarker

