package com.example.register

import com.example.cache.InMemoryCache
import com.example.cache.TokenCache
import com.example.plugins.Test
import com.example.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.util.UUID
import kotlin.uuid.Uuid

fun Application.configureRegisterRouting() {
    routing {
        post( "/register"){
            val receive = call.receive<RegisterReceiveRemote>()
            if(!receive.email.isValidEmail()){
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }
            if (InMemoryCache.userList.map {it.login }.contains(receive.login)){
                call.respond(HttpStatusCode.Conflict,"User already exists")
            }
            val token = UUID.randomUUID().toString()
            InMemoryCache.userList.add(receive)
            InMemoryCache.token.add(TokenCache(login = receive.login, token = token))
            call.respond (RegisterResponseRemote (token = token))
        }
    }
}