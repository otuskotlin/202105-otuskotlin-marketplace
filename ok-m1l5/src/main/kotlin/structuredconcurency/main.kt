package structuredconcurency

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun main() {
    val vocabulary = Vocabulary()

    coroutineScope {
        findWordsSlowly(vocabulary, "мог", "любимый")
    }
}

// Loads sequantially
// Time measure == time of each function * 2
suspend fun findWordsSlowly(vocabulary: Vocabulary, word: String, word2: String) {
    val word = vocabulary.find(word)
    val word2 = vocabulary.find(word2, withTime = 5000)

    println("Make some other stuff")
    println("Found $word && $word2")
    println("End some other stuff")
}


suspend fun findWordsAsync(vocabulary: Vocabulary, word: String, word2: String) {
    val deferred = GlobalScope.async { vocabulary.find(word) }
    val deferred2 = GlobalScope.async { vocabulary.find(word2, withTime = 5000) } // Resume (with GlobalScope)

    println("Make some other stuff")

//    println("deffered is active? ${deferred.isActive}; deffered2 is active? ${deferred2.isActive}")
//    runCatching {
        println("Found ${deferred.await()} && ${deferred2.await()}")
//    }.onFailure {
//        println("Deffered still running? ${deferred.isActive}")
//        println("Deffered is canceled? ${deferred.isCancelled}")
//        println("Deffered2 still running? ${deferred2.isActive}")
//        println("Deffered2 is canceled? ${deferred2.isCancelled}")
//    }.getOrThrow()
    println("End some other stuff")
}