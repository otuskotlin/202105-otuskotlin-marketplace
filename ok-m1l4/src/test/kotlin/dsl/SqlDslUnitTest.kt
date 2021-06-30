package dsl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqlDslUnitTest {

    @Test
    fun `simple select all from table`() {
        checkSQL("select * from table") {
            from("table")
        }
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        checkSQL("select col_a, col_b from table") {
            select("col_a", "col_b")
            from("table")
        }
    }

    @Test
    fun `select certain columns from table 1`() {
        checkSQL("select col_a, col_b from table") {
            select("col_a", "col_b")
            from("table")
        }
    }

    @Test
    fun `select with complex where condition with one condition`() {
        checkSQL("select * from table where col_a = 'id'") {
            from("table")
            where { "col_a" eq "id" }
        }
    }

    @Test
    fun `select with complex where condition with two conditions`() {
        checkSQL("select * from table where col_a != 0") {
            from("table")
            where { "col_a" nonEq 0 }
        }
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        checkSQL("select * from table where (col_a = 4 or col_b !is null)") {
            from("table")
            where {
                or {
                    "col_a" eq 4
                    "col_b" nonEq null
                }
            }
        }
    }

    private fun checkSQL(actual: String, sql: SqlSelectBuilder.() -> Unit) {
        val expected = query(sql).build()
        assertEquals(expected, actual)
    }
}