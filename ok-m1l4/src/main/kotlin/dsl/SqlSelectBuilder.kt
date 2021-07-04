package dsl

class SqlSelectBuilder {

    private lateinit var table: String
    private val columns = mutableListOf<String>()
    private val conditions = mutableListOf<String>()

    fun from(table: String) {
        this.table = table
    }

    fun select(vararg column: String) {
        columns.addAll(column)
    }

    fun build(): String {
        return toString()
    }

    fun where(block: SqlSelectBuilder.() -> Unit) {
        this.apply(block)
    }

    fun or(block: SqlSelectBuilder.() -> Unit) {
        this.apply(block)
    }

    infix fun String.eq(any: Any?) {
        when (any) {
            is String -> conditions.add("$this = '$any'")
            is Number -> conditions.add("$this = $any")
            else -> conditions.add("$this is $any")
        }
    }

    infix fun String.nonEq(any: Any?) {
        when (any) {
            is String -> conditions.add("$this != '$any'")
            is Number -> conditions.add("$this != $any")
            else -> conditions.add("$this !is $any")
        }
    }

    override fun toString(): String {

        val columnsQuery =
            if (columns.isEmpty()) {
                "*"
            } else {
                columns.joinToString(", ")
            }

        val columnsCondition =
            when (conditions.size) {
                0 -> ""
                1 -> " where " + conditions.first()
                else -> " where " + conditions.joinToString(" or ", "(", ")")
            }
        return "select $columnsQuery from $table$columnsCondition"
    }
}

fun query(block: SqlSelectBuilder.() -> Unit) = SqlSelectBuilder().apply(block)