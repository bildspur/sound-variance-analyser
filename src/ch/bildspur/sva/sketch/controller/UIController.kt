package ch.bildspur.sva.sketch.controller

import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.sketch.SVASketch
import ch.bildspur.sva.ui.SectorBar
import ch.bildspur.sva.util.center
import controlP5.ControlP5
import processing.core.PApplet
import processing.core.PVector

/**
 * Created by cansik on 25.09.16.
 */
class UIController(val sketch: SVASketch)
{
    val controlSpace = 20f
    val margin = 25f

    val cp5:ControlP5
    val sectorView = SectorBar(sketch, sketch.width - (2f * margin), 40f)

    init {
        cp5 = ControlP5(sketch)
    }

    fun init()
    {
        var hPos = controlSpace

        cp5.isAutoDraw = false

        sectorView.position = PVector(center(sectorView.width), hPos)
        sectorView.sectorSelected += {sectorSelected(it)}
        hPos += sectorView.height + controlSpace

        // test
        sectorView.addSector(Sector("Low", 0f, 0.5f))
        sectorView.addSector(Sector("High", 0.5f, 1f))

        // init cp5 controls

    }

    fun sectorSelected(sector:Sector)
    {
        PApplet.println("Selected: ${sector.name}")
    }

    fun render()
    {
        cp5.draw()
        sectorView.render()
    }

    internal fun center(width:Float):Float
    {
        return sketch.center(width, 0f, sketch.width.toFloat())
    }

    internal fun isOverControl(v: PVector): Boolean {
        return v.x >= sectorView.position.x && v.x < sectorView.position.x + sectorView.width
                && v.y >= sectorView.position.y && v.y < sectorView.position.y + sectorView.height
    }

    fun mousePressed() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        if (isOverControl(mouse))
            sectorView.mousePressed(PVector.sub(mouse, sectorView.position))
    }

    fun mouseDragged() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        if (isOverControl(mouse))
            sectorView.mouseDragged(PVector.sub(mouse, sectorView.position))
    }

    fun mouseMoved() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        sectorView.mouseMoved(PVector.sub(mouse, sectorView.position))
    }

    fun mouseReleased() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        if (isOverControl(mouse))
            sectorView.mouseReleased(PVector.sub(mouse, sectorView.position))
    }
}