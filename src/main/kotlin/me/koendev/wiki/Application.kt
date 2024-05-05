package me.koendev.wiki

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.koendev.wiki.plugins.*

val dotEnv = dotenv()

fun main() {

    embeddedServer(Netty, port = dotEnv["HOSTING_PORT"].toInt(), host = dotEnv["HOSTING_IP"], module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
