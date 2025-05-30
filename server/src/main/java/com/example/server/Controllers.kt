package com.example.server

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(val login: String, val password: String)

@Serializable
data class LoginResponseRemote(val token: String)

@Serializable
data class RegisterReceiveRemote(val login: String, val password: String, val email: String)

@Serializable
data class RegisterResponseRemote(val token: String)

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)
        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(TokenDTO(rowId = UUID.randomUUID().toString(), login = receive.login, token = token))
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}

class RegisterController(private val call: ApplicationCall) {
    suspend fun registerNewUser() {
        val receive = call.receive<RegisterReceiveRemote>()
        if (!receive.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }
        val userDTO = Users.fetchUser(receive.login)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                Users.insert(
                    UserDTO(
                        login = receive.login,
                        password = receive.password,
                        email = receive.email,
                        username = ""
                    )
                )
                Tokens.insert(TokenDTO(rowId = UUID.randomUUID().toString(), login = receive.login, token = token))
                call.respond(RegisterResponseRemote(token = token))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
        }
    }
}

fun String.isValidEmail() = this.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))