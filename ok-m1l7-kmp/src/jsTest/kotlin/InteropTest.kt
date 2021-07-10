package ru.otus.otuskotlin.marketplace.kmp

import org.w3c.dom.Location
import ru.otus.otuskotlin.marketplace.kmp.js.JsBigDecimal
import ru.otus.otuskotlin.marketplace.kmp.js.sorted
import ru.otus.otuskotlin.marketplace.kmp.js.useMathRound
import ru.otus.otuskotlin.marketplace.kmp.js.windowLocation
import kotlin.math.E
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InteropTest {

    @Test
    fun windowTest() {
        println("wL: $windowLocation")
        assertTrue("windowLocation must be a URL") {
            windowLocation.port.toInt() > 0
        }
    }

    @Test
    fun mathRoundTest() {
        assertEquals(3, useMathRound(E))
    }

    @Test
    fun sortedTest() {
        assertEquals(true, sorted(arrayOf(1,2,3,4,5)))
        assertEquals(false, sorted(arrayOf(4,1,2,3,4,5)))
    }

    @Test
    fun bigDecimalTest() {
        val bd = JsBigDecimal("${E* 10000}")
        println("BigDeciaml: ${bd.getValue()}")
        println("PrettyBigDeciaml: ${JsBigDecimal.getPrettyValue(bd.getValue(),1, "/")}")
        assertEquals("27183", bd.round().getValue())
    }

}
