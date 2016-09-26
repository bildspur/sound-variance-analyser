package ch.bildspur.sva.sketch.controller

import controlP5.ControlP5
import processing.core.PApplet

/**
 * Created by cansik on 25.09.16.
 */
class UIController(val sketch: PApplet)
{
    val cp5:ControlP5

    init {
        cp5 = ControlP5(sketch)
    }

    fun init()
    {
        cp5.isAutoDraw = false


    }

    fun render()
    {
        cp5.draw()
    }
}