package homework

import okhttp3.*

object Authorization : Authenticator {
    const val PERSONAL_TOKEN = "YOUR_TOKEN"
    const val USERNAME = "YOUR_LOGIN"

    override fun authenticate(route: Route?, response: Response): Request =
        Credentials.basic(USERNAME, PERSONAL_TOKEN)
            .let { creds ->
                response.request.newBuilder().header("Authorization", creds).build()
            }
}