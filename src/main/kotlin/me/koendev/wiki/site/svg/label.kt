package me.koendev.wiki.site.svg

import com.github.nwillc.ksvg.elements.Container

fun Container.label(px: Double, py: Double, text: String, font: String = "monospace", size: Int = 16, transformation: String = "") {
    text {
        x = px.toString()
        y = py.toString()
        body = text
        fontFamily = font
        fontSize = "${size}px"
        transform = transformation
    }
}