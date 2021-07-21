import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JUnit5Test {

    @BeforeAll
    fun tearUp() {
        println("Before All")
    }

    @Test
    fun junit5Test() {
        assertEquals("1", 1.toString())
    }
}
