package dsl

import models.Action
import kotlin.test.Test
import kotlin.test.assertEquals

class UserTestCase {
    @Test
    fun `test lambda function`() {
        sout {
            1 + 23
        }
    }

    @Test
    fun `test simple dsl`() {
        soutWithPrefix {
            "${time()}-info!"
        }
    }

    @Test
    fun `test simple user`() {
        val user = user {
            name {
                first = "Kirill"
                last = "Krylov"
            }
            birth {
                date = "2020-01-10"
            }
            contacts {
                email = "email@gmail.com"
                phone = "89111232233"
            }
            actions {
                add(Action.DELETE)
                add("UPDATE")

                +Action.CREATE
                +"READ"
            }
        }

        assertEquals(user.firstName, "Kirill")
    }
}
