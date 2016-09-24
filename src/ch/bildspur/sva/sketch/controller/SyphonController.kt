package ch.bildspur.sva.sketch.controller

import codeanticode.syphon.Syphon
import codeanticode.syphon.SyphonServer
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage

/**
 * Created by cansik on 21.09.16.
 */

class SyphonController(internal var sketch: PApplet) {

    internal var syphon : SyphonServer? = null

    fun setupSyphon(name:String)
    {
        syphon = SyphonServer(sketch, name)
    }

    fun sendImageToSyphon(p: PImage) {
        syphon!!.sendImage(p)
    }
}