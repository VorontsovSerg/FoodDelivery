package com.example.login

import com.example.cache.InMemoryCache
import com.example.cache.TokenCache
import com.example.plugins.Test
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.util.UUID
import kotlin.uuid.Uuid

fun Application.configureRouting() {
    routing {
        post("/login") {
            val receive = call.receive<LoginReceiveRemote>()
            if(InMemoryCache.userList.map {it.login}.contains(receive.login)) {
                val token = UUID.randomUUID().toString()
                InMemoryCache.token.add(TokenCache(login = receive.login, token = token))
                call.respond(LoginResponseRemote(token = token))
                return@post
            }
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}