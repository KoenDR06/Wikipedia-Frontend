package com.github.nwillc.ksvg.elements

import com.github.nwillc.ksvg.RenderMode

class RawElement(validation: Boolean): Element("", validation) {
    override fun render(appendable: Appendable, renderMode: RenderMode, isChild: Boolean) {
        if(body.isNotBlank()) {
            appendable.append(body)
            appendable.appendLine()
        }
    }
}
