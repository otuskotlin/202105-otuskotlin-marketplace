import org.junit.Test
import kotlin.test.assertEquals

class SimpleTestCase {
    @Test
    fun `sout without params`() {
        sout {
            1 + 413
        }
    }
    @Test
    fun `sout with prefix`() {
        soutWithPrefix {
            "${time()}: my line"
        }
    }

    @Test
    fun `HW test`() {
        println("dsfghfg")
        assertEquals(null, null)
    }
}