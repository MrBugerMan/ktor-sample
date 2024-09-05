package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

fun Application.configureSecurity() {
    authentication {
        jwt("auth-jwt") {
            realm = "ktor sample app"
            verifier(
                JWT
                    .require(JwtConfig.algorithm)
                    .withAudience(JwtConfig.audience)
                    .withIssuer(JwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(JwtConfig.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

object JwtConfig {
    private const val secret = "key"
    internal const val issuer = "issuer"
    internal const val audience = "audience"
    private const val validityInMs = 36_000_00 * 10 // 10 hours

    val algorithm = Algorithm.HMAC256(secret)

    fun makeToken(email: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("email", email)
        .sign(algorithm)

    fun isTokenValid(payload: DecodedJWT): Boolean {
        val expiresAt = payload.expiresAt
        return expiresAt != null && expiresAt.after(Date()) && payload.audience.contains(JwtConfig.audience)
    }
}




