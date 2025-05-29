package com.example.plugins

import com.example.cache.InMemoryCache
import com.example.cache.TokenCache
import com.example.login.LoginReceiveRemote
import com.example.login.LoginResponseRemote
import com.example.register.RegisterReceiveRemote
import com.example.register.RegisterResponseRemote
import com.example.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Test(val text: String)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(Test("Hello from Ktor!"))
        }
        post("/login") {
            val receive = call.receive<LoginReceiveRemote>()
            if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                val token = UUID.randomUUID().toString()
                InMemoryCache.token.add(TokenCache(login = receive.login, token = token))
                call.respond(LoginResponseRemote(token = token))
                return@post
            }
            call.respond(HttpStatusCode.BadRequest)
        }
        post("/register") {
            val receive = call.receive<RegisterReceiveRemote>()
            if (!receive.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }
            if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
            val token = UUID.randomUUID().toString()
            InMemoryCache.userList.add(receive)
            InMemoryCache.token.add(TokenCache(login = receive.login, token = token))
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}