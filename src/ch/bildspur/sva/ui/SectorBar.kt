package ch.bildspur.sva.ui

import ch.bildspur.event.Event
import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.sketch.SVASketch
import ch.bildspur.sva.util.center
import processing.core.PApplet
import processing.core.PVector
import java.awt.Color

/**
 * Created by cansik on 02.10.16.
 */
class SectorView (val sketch: SVASketch, val width:Float, val height:Float) {
    var position = PVector()
    val sectors = mutableListOf<Sector>()

    val sectorColors = listOf(
            Color(26, 188, 156),
            Color(52, 152, 219),
            Color(241, 196, 15),
            Color(231, 76, 60),
            Color(243, 156, 18),
            Color(44, 62, 80)
            )

    var selectedSector:Sector? = null

    val sectorSelected = Event<Sector>()

    fun init() {

    }

    fun render() {
        sketch.translate(position.x, position.y)
        drawSectors()
        visualizeVarianceOverTime()
        drawBorder()
        sketch.translate(0f, 0f)
    }

    internal fun drawBorder()
    {
        sketch.stroke(255)
        sketch.strokeWeight(1f)
        sketch.noFill()
        sketch.rect(0f, 0f, width, height)
    }

    internal fun visualizeVarianceOverTime()
    {
        val variance = sketch.sva.varianceOverTime()

        sketch.noStroke()
        sketch.fill(255f, 100f)
        sketch.rect(0f, 0f, PApplet.map(variance, 0f, 1f, 0f, width), height)
    }

    internal fun drawSectors()
    {
        for ((index, sector) in sectors.withIndex())
        {
            val sectorColor = sectorColors[index]

            val x = PApplet.map(sector.start, 0f, 1f, 0f, width)
            val y = 0f
            val w = PApplet.map(sector.length, 0f, 1f, 0f, width)
            val h = height

            // draw color
            sketch.noStroke()
            sketch.fill(sectorColor.rgb)
            sketch.rect(x, y, w, h)

            // draw label
            sketch.fill(255f)
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER)
            sketch.text(sector.name,
                    sketch.center(0f, x, x + w),
                    sketch.center(0f, y, y + h))
        }
    }

    fun mousePressed(mouse:PVector) {

    }

    fun mouseDragged(mouse:PVector) {

    }

    fun mouseReleased(mouse:PVector) {

    }
}