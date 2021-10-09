package ru.otus.otuskotlin.marketplace.backend.repo.sql

import java.io.File
import java.io.FileInputStream
import java.util.*

// Custom properties for database control
object SqlHostingProperties {
    // SQL
    const val SQL_URL = "jdbcUrl"
    const val SQL_DROP_DB = "ok.mp.sql_drop_db" // withDefaultValue(false)
    const val SQL_FAST_MIGRATION = "ok.mp.sql_fast_migration" // withDefaultValue(true)
}

fun loadFromFile(propFileName: String): Properties {
    val propFile = File(propFileName)
    val fileStream = if (propFile.isFile) {
        FileInputStream(propFile)
    } else {
        SqlHostingProperties::class.java.classLoader.getResourceAsStream(propFileName)
    }
    return if (fileStream != null) {
        Properties().apply { load(fileStream) }
    } else {
        throw IllegalArgumentException("Cannot find property file: $propFileName")
    }
}

fun Properties.property(key: String): String? = getProperty(key, null)

fun Properties.withoutCustom() = Properties().also { props ->
    filterNot { it.key.toString().startsWith("ok.mp.") }.forEach {
        props[it.key] = it.value
    }
}
