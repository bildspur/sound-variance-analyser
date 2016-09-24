package ch.bildspur.sva.sketch

import ch.bildspur.sva.sketch.controller.SyphonController
import ch.bildspur.sva.sketch.controller.UIController
import ch.bildspur.sva.sound.LoopRingBuffer
import ch.bildspur.sva.sound.SoundVarianceAnalyser
import ddf.minim.AudioPlayer
import processing.core.PApplet
import processing.core.PConstants
import processing.opengl.PJOGL
import processing.video.Movie

/**
 * Created by cansik on 25.09.16.
 */
class SVASketch : PApplet()
{
    internal val OUTPUT_WIDTH = 300
    internal val OUTPUT_HEIGHT = 600

    internal val NAME = "Sound Variance Analyser"

    internal val FRAME_RATE = 30f

    internal var sva : SoundVarianceAnalyser = SoundVarianceAnalyser(this)

    internal val syphon = SyphonController(this)
    internal val ui = UIController(this)

    override fun settings()
    {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT, PConstants.P2D)
        PJOGL.profile = 1
    }

    override fun setup()
    {
        frameRate(FRAME_RATE)

        surface.setTitle(NAME)
        syphon.setupSyphon(NAME)

        sva.init()
        ui.init()
    }

    override fun draw()
    {
        background(50)
        sva.listen()

        ui.render()
    }

    fun movieEvent(m: Movie) {
        m.read()
    }
}