package homework.hard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import homework.hard.dto.Dictionary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.util.concurrent.Executors

class DictionaryApi(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    // Если использовать этот Dispatcher, то "Searching..." выводится сразу для всех слов (132 слова).
    // но программа не завершается после печати всех значений в консоль.
    private val myDispatcher = Executors.newFixedThreadPool(132).asCoroutineDispatcher()

    suspend fun findWord(locale: Locale, word: String): Dictionary =  // make something with context
        withContext(Dispatchers.Default) {
            val url = "$DICTIONARY_API/${locale.code}/$word"
            println("Searching $url")
            val result = getBody(HttpClient.get(url).execute()).first()
            println("end $word")
            return@withContext result
        }

//    fun findWord(locale: Locale, word: String): Dictionary { // make something with context
//        val url = "$DICTIONARY_API/${locale.code}/$word"
//        println("Searching $url")
//
//        return getBody(HttpClient.get(url).execute()).first()
//    }


    private fun getBody(response: Response): List<Dictionary> {
        if (!response.isSuccessful) {
            throw RuntimeException("Not found word")
        }

        return response.body?.let {
            objectMapper.readValue(it.string())
        } ?: throw RuntimeException("Body is null by some reason")
    }
}