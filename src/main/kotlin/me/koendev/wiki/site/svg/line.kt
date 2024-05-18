package me.koendev.wiki.site.svg

import com.github.nwillc.ksvg.elements.Container

fun Container.line(fromX: Double, fromY: Double, toX: Double, toY: Double, mode: String) {
    val control2Y = fromY
    val control2X = fromX + 0.5 * (toX - fromX)
    val control1Y = toY
    val control1X = toX - 0.5 * (toX - fromX)

    path {
        cssClass = "arrow"
        d = "M $fromX,$fromY C $control1X,$control1Y $control2X,$control2Y $toX $toY"
        strokeWidth = "1px"
        fill = "none"

        stroke = when (mode) {
            "child"     -> "#ec0"
            "parent"    -> "#30a"
            else        -> throw Exception("Node mode $mode is not recognized.")
        }
    }
}