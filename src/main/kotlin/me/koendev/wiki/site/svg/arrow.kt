package me.koendev.wiki.site.svg

import com.github.nwillc.ksvg.elements.Container
import kotlin.math.pow
import kotlin.math.sqrt


fun Container.arrow(fromX: Double, fromY: Double, toX: Double, toY: Double, mode: String) {
    val radius = sqrt((fromX - toX).pow(2) + (fromY - toY).pow(2))

    path {
        cssClass = "arrow"
        d = "M $fromX,$fromY A $radius,$radius 0 0 1 $toX,$toY"
        strokeWidth = "1px"
        fill = "none"

        stroke = when (mode) {
            "child"     -> "#ec0"
            "parent"    -> "#30a"
            else        -> throw Exception("Node mode $mode is not recognized.")
        }
    }
}