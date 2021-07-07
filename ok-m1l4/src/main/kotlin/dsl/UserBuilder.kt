package dsl

import models.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@UserDsl
class UserContext {
    var first: String = ""
    var second: String = ""
    var last: String = ""
}

@UserDsl
class ContactsContext {
    var email: String = ""
    var phone: String = ""
}

@UserDsl
class ActionsDsl {
    private val _actions: MutableSet<Action> = mutableSetOf()

    val actions: Set<Action>
        get() = _actions.toSet()

    fun add(action: Action) {
        _actions.add(action)
    }

    fun add(action: String) = add(Action.valueOf(action))

    operator fun Action.unaryPlus() = add(this)

    operator fun String.unaryPlus() = add(this)
}

@Suppress("unused")
@UserDsl
class AvailableCreator {
    private val myAvailableList: MutableList<LocalDateTime> = mutableListOf()

    val availableList: List<LocalDateTime>
        get() = myAvailableList.toList()

    fun sunday(time:String) = dayAndTimeOfWeek(DayOfWeek.SUNDAY, time)

    fun monday(time:String) = dayAndTimeOfWeek(DayOfWeek.MONDAY, time)

    fun tuesday(time:String) = dayAndTimeOfWeek(DayOfWeek.TUESDAY, time)

    fun wednesday(time:String) = dayAndTimeOfWeek(DayOfWeek.WEDNESDAY, time)

    fun thursday(time:String) = dayAndTimeOfWeek(DayOfWeek.THURSDAY, time)

    fun friday(time:String) = dayAndTimeOfWeek(DayOfWeek.FRIDAY, time)

    fun saturday(time:String) = dayAndTimeOfWeek(DayOfWeek.SATURDAY, time)

    private fun dayAndTimeOfWeek(day: DayOfWeek, time: String) {
        val dDay = dayOfWeek(day)
        val dTime = parseTime(time)

        myAvailableList.add(LocalDateTime.of(dDay, dTime))
    }

    private fun dayOfWeek(day: DayOfWeek): LocalDate {
        return LocalDate.now().with(TemporalAdjusters.next(day))
    }

    /**
     *
     * @param time format __hh:mm__
     */
    private fun parseTime(time: String): LocalTime {
//        val parsed = time.split(":")
//            .map { it.toInt() }
//        return LocalTime.of(parsed[0], parsed[1])
        return time.split(":")
            .map { it.toInt() }
            .let { LocalTime.of(it[0], it[1]) }
    }
}

@UserDsl
class UserBuilder {
    var id = UserId.random()

    var firstName = ""
    var secondName = ""
    var lastName = ""

    val available = mutableListOf<LocalDateTime>()
    var email = Email.NONE
    var phone = Phone.NONE

    val actions = mutableSetOf<Action>()

    @UserDsl
    fun name(block: UserContext.() -> Unit) {
        val context = UserContext().apply(block)

        firstName = context.first
        secondName = context.second
        lastName = context.last
    }

    @UserDsl
    fun actions(block: ActionsDsl.() -> Unit) {
        val context = ActionsDsl().apply(block)

        actions.addAll(context.actions)
    }

    @UserDsl
    fun available(block: AvailableCreator.() -> Unit) {
        val context = AvailableCreator().apply(block)

        available.addAll(context.availableList)
    }

    fun build() = User(
        id = id,
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        available = available,
        email = email,
        phone = phone,
        actions = actions,
    )
}

@UserDsl
fun UserBuilder.contacts(block: ContactsContext.() -> Unit) {
    val context = ContactsContext().apply(block)

    email = Email(context.email)
    phone = Phone(context.phone)
}

@UserDsl
fun user(block: UserBuilder.() -> Unit) = UserBuilder().apply(block).build()

//fun user(block: UserBuilder.() -> Unit): User {
//    val builder = UserBuilder()
//    builder.block()
//    return builder.build()
//}

@DslMarker
annotation class UserDsl
