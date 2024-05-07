package me.koendev.wiki.site.svg

import com.github.nwillc.ksvg.elements.Container

fun Container.line(fromX: Double, fromY: Double, toX: Double, toY: Double, mode: String) {
    path {
        cssClass = "arrow"
        d = "M $fromX,$fromY L $toX,$toY"
        strokeWidth = "1px"
        fill = "none"

        stroke = when (mode) {
            "child"     -> "#ec0"
            "parent"    -> "#30a"
            else        -> throw Exception("Node mode $mode is not recognized.")
        }
    }
}