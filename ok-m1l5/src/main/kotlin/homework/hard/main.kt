package homework.hard

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() {
    val dictionaryApi = DictionaryApi()
    val words = FileReader.readFile().split(" ", "\n").toSet()

    val dictionaries = findWords(dictionaryApi, words, Locale.EN)

    dictionaries.map { dictionary ->
        print("For word ${dictionary.word} i found examples: ")
        println(dictionary.meanings.map { definition -> definition.definitions.map { it.example } })
    }
}

private fun findWords(dictionaryApi: DictionaryApi, words: Set<String>, locale: Locale) = runBlocking { // make some suspensions and async
    words.map {
        async { dictionaryApi.findWord(locale, it) }
    }.awaitAll()
}


//private fun findWords(dictionaryApi: DictionaryApi, words: Set<String>, locale: Locale) = // make some suspensions and async
//    words.map {
//        dictionaryApi.findWord(locale, it)
//    }

object FileReader {
    fun readFile(): String =
        File(
            this::class.java.classLoader.getResource("words.txt")?.toURI() ?: throw RuntimeException("Can't read file")
        ).readText()
}
