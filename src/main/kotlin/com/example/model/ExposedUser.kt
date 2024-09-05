package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ExposedUser(val email: String, val password: String)
