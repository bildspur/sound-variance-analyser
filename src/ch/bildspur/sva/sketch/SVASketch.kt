package ch.bildspur.sva.sketch

import ch.bildspur.sva.sketch.controller.SyphonController
import ch.bildspur.sva.sketch.controller.UIController
import ch.bildspur.sva.sound.SoundVarianceAnalyser
import processing.core.PApplet
import processing.core.PConstants
import processing.opengl.PJOGL
import processing.video.Movie

/**
 * Created by cansik on 25.09.16.
 */
class SVASketch : PApplet()
{
    internal val OUTPUT_WIDTH = 600
    internal val OUTPUT_HEIGHT = 450

    internal val NAME = "Sound Variance Analyser"

    internal val FRAME_RATE = 30f

    internal var sva : SoundVarianceAnalyser = SoundVarianceAnalyser(this)

    internal val syphon = SyphonController(this)
    internal var ui:UIController? = null

    override fun settings()
    {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT, PConstants.P2D)
        PJOGL.profile = 1
    }

    override fun setup()
    {
        smooth()
        frameRate(FRAME_RATE)

        surface.setTitle(NAME)
        syphon.setupSyphon(NAME)

        ui = UIController(this)

        sva.init()
        ui!!.init()
    }

    override fun draw()
    {
        background(55f)
        sva.listen()

        ui!!.render()
    }

    fun movieEvent(m: Movie) {
        m.read()
    }

    override fun mousePressed() {
        ui!!.mousePressed()
    }

    override fun mouseDragged() {
        ui!!.mouseDragged()
    }

    override fun mouseMoved() {
        ui!!.mouseMoved()
    }

    override fun mouseReleased() {
        ui!!.mouseReleased()
    }
}