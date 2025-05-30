package com.example.server

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val login = varchar("login", 25)
    val password = varchar("password", 25)
    val username = varchar("username", 30)
    val email = varchar("email", 25).nullable()

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                select { Users.login eq login }.singleOrNull()?.let { row ->
                    UserDTO(
                        login = row[login],
                        password = row[password],
                        username = row[username],
                        email = row[email]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}