package me.koendev.wiki.site

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.koendev.wiki.plugins.articleService
import me.koendev.wiki.println

fun Routing.webRouting() {
    articlePage()

    get("/") {
        call.respondRedirect("/article")
    }
    get("/article") {
        val title = articleService.readRandomTitle()
        title.println()
        call.respondRedirect("/article/$title")
    }
}