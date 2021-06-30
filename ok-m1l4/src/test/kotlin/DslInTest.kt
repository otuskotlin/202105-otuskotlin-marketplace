import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DslInTest {
    @Test
    fun `HW test`() {
        doTest {
            data {
                name = "Kirill"
                age = "292"
            }
            assertions {
                add { assertEquals(it.name, "Kirill") }
                add { assertEquals(it.age, "292") }
            }
        }
    }

    class UserContext {
        var name: String = ""
        var age: String = ""
    }

    class AssertionsContext {
        val assertions: MutableList<Function1<SimpleUser, Unit>> = mutableListOf()

        fun add(f: (SimpleUser) -> Unit) {
            assertions.add(f)
        }
    }

    class TestContext {
        var data: SimpleUser? = null

        fun data(block: UserContext.() -> Unit) {
            val context = UserContext().apply(block)
            data = SimpleUser(context.name, context.age)
        }

        fun assertions(block: AssertionsContext.() -> Unit) {
            assertNotNull(data)
            val context = AssertionsContext().apply(block)
            context.assertions.forEach {
                it(data!!)
            }
        }
    }

    private fun doTest(block: TestContext.() -> Unit) {
        TestContext().apply(block)
    }
}