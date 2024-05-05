package me.koendev.wiki.site

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import com.github.nwillc.ksvg.elements.SVG
import io.ktor.server.response.*
import kotlinx.html.*
import kotlinx.html.unsafe
import me.koendev.wiki.plugins.articleService
import me.koendev.wiki.plugins.linkService
import me.koendev.wiki.site.svg.arrow
import me.koendev.wiki.site.svg.node
import java.util.*
import kotlin.math.*

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

            val viewBoxHeight = 1080
            val viewBoxWidth = 1920
            val minimumCircleSize = 50
            val nodeSpacing = 10

            var numberOfNodes = children.size + parents.size - 1
            if (numberOfNodes == 0) numberOfNodes = 1
            var radius = (numberOfNodes * nodeSpacing) / (2*PI)
            if (radius < minimumCircleSize) {
                radius = minimumCircleSize.toDouble()
            }

            val svg: SVG = SVG.svg(true) {
                height = "$viewBoxHeight"
                width = "$viewBoxWidth"

                node(viewBoxWidth / 2.0, viewBoxHeight / 2.0, "source")

                for ((index, child) in children.withIndex()) {
                    val fromX = viewBoxWidth / 2.0
                    val fromY = viewBoxHeight / 2.0
                    var toX = fromX + radius*cos((index.toDouble()/numberOfNodes) * 2 * PI)
                    var toY = fromY + radius*sin((index.toDouble()/numberOfNodes) * 2 * PI)
                    if (toX < 0) toX = 5.0
                    if (toX > viewBoxWidth) toX = viewBoxWidth - 5.0
                    if (toY < 0) toY = 5.0
                    if (toY > viewBoxHeight) toY = viewBoxHeight - 5.0


                    node(toX, toY, "child")
                    arrow(fromX, fromY, toX, toY, "child")
                    // TODO Put label next to node
                }

                for ((index, parent) in parents.withIndex()) {
                    val fromX = viewBoxWidth / 2.0
                    val fromY = viewBoxHeight / 2.0
                    var toX = fromX + radius*cos((-index.toDouble()/numberOfNodes) * 2 * PI)
                    var toY = fromY + radius*sin((-index.toDouble()/numberOfNodes) * 2 * PI)
                    if (toX < 0) toX = 5.0
                    if (toX > viewBoxWidth) toX = viewBoxWidth - 5.0
                    if (toY < 0) toY = 5.0
                    if (toY > viewBoxHeight) toY = viewBoxHeight - 5.0


                    node(toX, toY, "parent")
                    arrow(fromX, fromY, toX, toY, "parent")
                    // TODO Put label next to node
                }
            }

            call.respondHtml(HttpStatusCode.OK) {
                head {

                }
                /*body {
                h2 {
                    +"Children of $currentArticle: "
                }
                ul {
                    for (child in children) {
                        li {
                            a {
                                href = "/article/$child"
                                +articleService.read(child)!!
                            }
                        }
                    }
                }

                h2 {
                    +"Parents of $currentArticle: "
                }
                ul {
                    for (parent in parents) {
                        li {
                            a {
                                href = "/article/$parent"
                                +articleService.read(parent)!!
                            }
                        }
                    }
                }
            }*/

                body {
                    unsafe {
                        raw(svg.toString())
                    }
                }
            }
        }
    }
}

