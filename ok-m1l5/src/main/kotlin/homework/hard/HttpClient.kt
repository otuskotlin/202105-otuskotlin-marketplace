package homework.hard

import okhttp3.OkHttpClient
import okhttp3.Request


object HttpClient: OkHttpClient() {
    fun get(uri: String) =
        Request.Builder().url(uri).build()
            .let {
                newCall(it)
            }
    }
