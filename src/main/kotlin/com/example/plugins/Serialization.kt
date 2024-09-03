package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() { // а на куя это, если могу установку сделать в Routing????
    install(ContentNegotiation) {
        json()
    }
}
