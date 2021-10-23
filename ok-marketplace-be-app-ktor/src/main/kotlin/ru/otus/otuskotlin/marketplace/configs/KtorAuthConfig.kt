package ru.otus.otuskotlin.marketplace.configs

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import java.util.*

data class KtorAuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
) {
    constructor(environment: ApplicationEnvironment): this(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )

    companion object {
        const val ID_CLAIM = "id"
        const val GROUPS_CLAIM = "groups"
        const val FNAME_CLAIM = "fname"
        const val MNAME_CLAIM = "mname"
        const val LNAME_CLAIM = "lname"

        val TEST = KtorAuthConfig(
                secret = "secret",
                issuer = "OtusKotlin",
                audience = "ad-users",
                realm = "Access to Ads"
            )
    }
}
