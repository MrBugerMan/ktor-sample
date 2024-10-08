package com.example.plugins

import com.example.model.ExposedUser
import com.example.plugins.JwtConfig.makeToken
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.modules.SerializersModule

fun Application.configureRouting(userService: UserService) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        // Create user (public route)
        post("/users") {
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            //val token = makeToken(user.email)
            call.respond(HttpStatusCode.Created, "$id --!!!-- $user --!!!-- ${makeToken(user.email)}")
        }

        // Protected routes
        authenticate("auth-jwt") {
            // Read user
            get("/users/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            // Update user
            put("/users/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = call.receive<ExposedUser>()
                userService.update(id, user)
                call.respond(HttpStatusCode.OK)
            }

            // Delete user
            delete("/users/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}
