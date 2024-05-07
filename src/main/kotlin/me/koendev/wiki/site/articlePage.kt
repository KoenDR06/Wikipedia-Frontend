package me.koendev.wiki.site

import com.github.nwillc.ksvg.elements.SVG
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.unsafe
import me.koendev.wiki.plugins.articleService
import me.koendev.wiki.plugins.linkService
import me.koendev.wiki.site.svg.label
import me.koendev.wiki.site.svg.line
import me.koendev.wiki.site.svg.node
import java.util.*

fun Routing.articlePage() {
    get("/article/{article}") {
        val articleTitle = call.parameters["article"] ?: throw Exception()
        val articleId = articleService.read(articleTitle.lowercase(Locale.getDefault()))

        if (articleId == null) {
            call.respondText("404 Not Found", status = HttpStatusCode.NotFound)
        } else {
            val children = linkService.readChildren(articleId)

            val parents = linkService.readParents(articleId)
            val currentArticle = articleService.read(articleId)!!

            val viewBoxHeight = 1080.0
            val viewBoxWidth = 1920.0

            val svg: SVG = SVG.svg(true) {
                height = "$viewBoxHeight"
                width = "$viewBoxWidth"

                g {
                    for ((index, childId) in children.withIndex()) {
                        val fromX = viewBoxWidth / 2.0
                        val fromY = viewBoxHeight / 2.0
                        val toX = viewBoxWidth / children.size * index
                        val toY = fromY + 200


                        line(fromX, fromY, toX, toY, "child")
                        node(toX, toY, "child")
                        /*g {
                            label(toX, toY, articleService.read(childId)!!)
                        }*/
                    }
                }

                g {
                    for ((index, parentId) in parents.withIndex()) {
                        val fromX = viewBoxWidth / 2.0
                        val fromY = viewBoxHeight / 2.0
                        val toX = viewBoxWidth / parents.size * index
                        val toY = fromY - 200

                        line(fromX, fromY, toX, toY, "parent")
                        node(toX, toY, "parent")
                        /*g {
                            label(toX, toY, articleService.read(parentId)!!)
                        }*/
                    }
                }

                g {
                    node(viewBoxWidth / 2.0, viewBoxHeight / 2.0, "source")
                    label(viewBoxWidth / 2.0, viewBoxHeight / 2.0, currentArticle)
                }
            }

            call.respondHtml(HttpStatusCode.OK) {
                head {

                }

                body {
                    unsafe {
                        raw(svg.toString())
                    }
                }
            }
        }
    }
}