package me.koendev.wiki

import io.ktor.http.*

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

val HttpStatusCode.Companion.IAmATeaPot get() = HttpStatusCode(418, "I'm a tea pot")