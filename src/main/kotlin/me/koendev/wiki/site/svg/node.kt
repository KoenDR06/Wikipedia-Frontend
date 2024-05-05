package me.koendev.wiki.site.svg

import com.github.nwillc.ksvg.elements.Container

fun Container.node(x: Double, y: Double, mode: String = "child", radius: Int = 3) {
    circle {
        cssClass = "node"
        cx = x.toString()
        cy = y.toString()
        r = radius.toString()

        if (mode == "child") {
            fill = "#ffc"
            stroke = "#ec0"
        } else if (mode == "source") {
            fill = "#cfc"
            stroke = "#090"
        } else if (mode == "parent") {
            fill = "#aaf"
            stroke = "#30a"
        } else {
            throw Exception("Node mode $mode is not recognized.")
        }
    }
}