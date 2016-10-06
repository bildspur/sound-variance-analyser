package ch.bildspur.sva.ui

import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.util.Rectangle
import ch.bildspur.sva.util.center
import processing.core.PApplet
import processing.core.PVector
import java.awt.Color

/**
 * Created by cansik on 02.10.16.
 */
class SectorView (val sector: Sector, val parent: SectorBar, val color: Color) {

    var isHover = false
    var isSelected = false

    fun render(g:PApplet)
    {
        val rect = rectangle()

        // draw color
        g.noStroke()
        g.fill(if(isHover) color.brighter().rgb else color.rgb)
        g.rect(rect.x, rect.y, rect.width, rect.height)

        // draw label
        g.fill(if(isSelected) 255f else 0f)
        g.textAlign(PApplet.CENTER, PApplet.CENTER)
        g.text(sector.name,
                g.center(0f, rect.x, rect.x + rect.width),
                g.center(0f, rect.y, rect.y + rect.height))
    }

    fun rectangle() : Rectangle {
        return Rectangle(PApplet.map(sector.start, 0f, 1f, 0f, parent.width),
                0f,
                PApplet.map(sector.length, 0f, 1f, 0f, parent.width),
                parent.height)
    }

    fun isInside(m : PVector) : Boolean
    {
        return rectangle().isInside(m)
    }
}