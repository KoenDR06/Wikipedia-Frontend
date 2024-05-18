package me.koendev.wiki.site

import com.github.nwillc.ksvg.elements.SVG
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
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

            val viewBoxHeight = 2160.0*2
            val viewBoxWidth = 3840.0*2

            val svg: SVG = SVG.svg(true) {
                height = "$viewBoxHeight"
                width = "$viewBoxWidth"

                g {
                    val fromX = viewBoxWidth / 2.0
                    val fromY = viewBoxHeight / 2.0
                    val toY = fromY + 500
                    val xOffset = children.size * 20.0 / 2 - fromX
                    for ((index, childId) in children.withIndex()) {
//                        val toX = (viewBoxWidth-300) / children.size * index + 0.5 * (viewBoxWidth-300) / children.size + 150
                        val toX = index * 20.0 + xOffset


                        line(fromX, fromY, toX, toY, "child")
                        node(toX, toY, "child")
                        g {
                            label(
                                px = 0.0,
                                py = 0.0,
                                text = articleService.read(childId)!!,
                                transformation = "translate($toX, $toY) rotate(45, 0, 0)"
                            )
                        }
                    }
                }

                g {
                    val fromX = viewBoxWidth / 2.0
                    val fromY = viewBoxHeight / 2.0
                    val toY = fromY - 500
                    val xOffset = parents.size * 20.0 / 2 - fromX
                    for ((index, parentId) in parents.withIndex()) {
//                        val toX = (viewBoxWidth-300) / parents.size * index + 0.5 * (viewBoxWidth-300) / parents.size + 150
                        val toX = index * 20.0 + xOffset

                        line(fromX, fromY, toX, toY, "parent")
                        node(toX, toY, "parent")
                        g {
                            label(
                                px = 0.0,
                                py = 0.0,
                                text = articleService.read(parentId)!!,
                                transformation = "translate($toX, $toY) rotate(-45, 0, 0)"
                            )
                        }
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