package me.koendev.wiki.site

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.webRouting() {
    articlePage()

    get("/") {
        call.respondRedirect("/article")
    }
}