package ch.bildspur.sva.ui

import ch.bildspur.event.Event
import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet
import processing.core.PVector
import java.awt.Color

/**
 * Created by cansik on 02.10.16.
 */
class SectorBar(val sketch: SVASketch, val width:Float, val height:Float) {
    var position = PVector()
    internal val sectorViews = mutableListOf<SectorView>()

    val sectorColors = listOf(
            Color(26, 188, 156),
            Color(52, 152, 219),
            Color(241, 196, 15),
            Color(231, 76, 60),
            Color(243, 156, 18),
            Color(44, 62, 80)
            )

    val sectorSelected = Event<Sector>()

    init {

    }

    fun render() {
        sketch.translate(position.x, position.y)
        drawSectors()
        visualizeVarianceOverTime()
        drawBorder()
        sketch.translate(-position.x, -position.y)
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
        val variance = sketch.sva.varianceOverTimeNorm()
        val value = Math.min(Math.max(0f, variance), 1f)

        var color = Color.white

        if(variance < 0.0 || variance > 1.0)
            color = Color(231, 76, 60)

        sketch.noStroke()
        sketch.fill(color.rgb, 200f)
        sketch.rect(0f, height * 0.85f, PApplet.map(value, 0f, 1f, 0f, width), height * 0.15f)
    }

    internal fun drawSectors()
    {
        for (sectorView in sectorViews)
            sectorView.render(sketch)
    }

    fun addSector(sector: Sector)
    {
        sectorViews.add(SectorView(sector, this, sectorColors[sectorViews.size]))
    }

    fun removeSector(sector: Sector)
    {
        sectorViews.removeAll { e -> e.sector == sector }
    }

    fun mousePressed(mouse:PVector) {
        for (sectorView in sectorViews) {
            sectorView.isSelected = sectorView.isInside(mouse)

            if(sectorView.isSelected)
                sectorSelected(sectorView.sector)
        }
    }

    fun mouseMoved(mouse: PVector) {
        for (sectorView in sectorViews)
            sectorView.isHover = sectorView.isInside(mouse)
    }

    fun mouseDragged(mouse:PVector) {

    }

    fun mouseReleased(mouse:PVector) {

    }
}