package me.koendev.wiki.plugins

import me.koendev.wiki.IAmATeaPot
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.koendev.wiki.site.webRouting

fun Application.configureRouting() {
    install(DoubleReceive)
    install(Resources)
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondText("404 Not Found", status = HttpStatusCode.NotFound)
        }
        status(HttpStatusCode.IAmATeaPot) { call, _ ->
            call.respondText("418 I'm a teapot", status = HttpStatusCode.IAmATeaPot)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        webRouting()

        get(Regex("(?<url>.*)/")) {
            call.respondRedirect(("/" + call.parameters["url"]))
        }
    }
}
