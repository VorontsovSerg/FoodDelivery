package com.example.server

import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    Database.connect(
        url = "jdbc:postgresql://localhost:5432/fooddelivery_db",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "your_postgres_password"
    )

    transaction {
        SchemaUtils.create(Users, Tokens)
    }

    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }
    }
}