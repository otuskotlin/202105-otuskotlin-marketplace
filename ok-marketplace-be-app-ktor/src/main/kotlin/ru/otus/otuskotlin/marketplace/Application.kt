package ru.otus.otuskotlin.marketplace

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.configs.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.otus.otuskotlin.marketplace.features.restFeature
import ru.otus.otuskotlin.marketplace.features.wsFeature

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("UNUSED_PARAMETER") // Referenced in application.conf
@JvmOverloads
fun Application.module(config: AppKtorConfig = AppKtorConfig(environment)) {
    // Generally not needed as it is replaced by a `routing`
    install(Routing)

//    https://github.com/imalik8088/ktor-starter/blob/master/src/Application.kt
    install(Authentication) {
        jwt("auth-jwt") {
            realm = config.auth.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.auth.secret))
                    .withAudience(config.auth.audience)
                    .withIssuer(config.auth.issuer)
                    .build()
            )
            validate { jwtCredential: JWTCredential ->
                // Лучше использовать IValidator
                when {
                    !jwtCredential.payload.audience.contains(config.auth.audience) -> {
                        log.error("Unsupported audience in JWT token ${jwtCredential.payload.audience}. Must be ${config.auth.audience}")
                        null
                    }
                    jwtCredential.payload.issuer != config.auth.issuer -> {
                        log.error("Wrong issuer in JWT token ${jwtCredential.payload.issuer}. Must be ${config.auth.issuer}")
                        null
                    }
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        log.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }
    restFeature(config)
    wsFeature(config)
}
