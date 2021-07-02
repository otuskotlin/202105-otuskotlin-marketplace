package homework

import okhttp3.OkHttpClient
import okhttp3.Request


class HttpClient(auth: Authorization) {
    private val client = OkHttpClient.Builder().authenticator(auth).build()

    fun get(uri: String) =
        Request.Builder().url(uri).build()
            .let {
                client.newCall(it).execute()
            }
}