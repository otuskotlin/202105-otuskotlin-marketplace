import dsl.contacts
import dsl.user
import models.Action
import org.junit.Test
import kotlin.test.assertEquals

class UserTestCase {
    @Test
    fun `sout without params`() {
        val user = user {
            name {
                first = "Kirill"
                last = "Krylov"
            }
            contacts {
                email = "email@gmail.com"
                phone = "898876312"
            }
            actions {
                add(Action.DELETE)
                add("UPDATE")

                +Action.CREATE
                +"READ"
            }
            available {
                tuesday("12:00")
                friday("21:30")
                monday("00:01")
            }
        }

        assertEquals(user.firstName, "Kirill")
        println(user)
    }
}