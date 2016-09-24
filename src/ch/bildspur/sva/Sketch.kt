package ch.bildspur.sva

import ch.bildspur.sva.sound.LoopRingBuffer
import ch.bildspur.sva.sound.SoundVarianceAnalyser
import ddf.minim.AudioPlayer
import ddf.minim.analysis.FFT
import processing.core.PApplet
import processing.core.PConstants
import processing.opengl.PJOGL
import processing.video.Movie

/**
 * Created by cansik on 21.09.16.
 */
class Sketch : PApplet()
{
    internal val OUTPUT_WIDTH = 640
    internal val OUTPUT_HEIGHT = 480

    internal val FRAME_RATE = 30f

    internal var song: AudioPlayer? = null

    internal var onSetRadius = 0f

    internal var sva : SoundVarianceAnalyser = SoundVarianceAnalyser(this)

    internal var varianceTracker = LoopRingBuffer(50)

    internal var lowMovie = Movie(this, sketchPath("data/low.mov"))
    internal var midMovie = Movie(this, sketchPath("data/mid.mov"))
    internal var highMovie = Movie(this, sketchPath("data/high.mov"))

    internal var activeMovie = lowMovie

    internal val syphon = SyphonController(this)

    override fun settings()
    {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT, PConstants.P2D)
        PJOGL.profile = 1
    }

    override fun setup()
    {
        frameRate(FRAME_RATE)

        syphon.setupSyphon()

        surface.setTitle("Sound Variance Analyser")

        //song = sva.minim.loadFile("justice-minimix.mp3", 2048)
        song = sva.minim.loadFile("techhouse-minimix.mp3", 2048)
        //song = sva.minim.loadFile("dubstep-minimix.mp3", 2048)

        song!!.play()

        sva.init(song!!)

        //sva.init()

        lowMovie.loop()
        midMovie.loop()
        highMovie.loop()
    }

    override fun draw()
    {
        background(50)
        sva.listen()

        drawUI()
    }

    fun movieEvent(m: Movie) {
        m.read()
    }

    internal fun drawUI() {
        // live input
        fill(255f, 238f, 173f)
        textAlign(PConstants.LEFT, PConstants.CENTER)
        text("live input:", 40f, 50f)

        drawLevel(130f, 10f, 20f, 100f, sva.source.mix.level())
        drawBuffer(180f, 10f, 400f, 100f, sva.source.mix.toArray())

        // analysis
        fill(255f, 238f, 173f)
        textAlign(PConstants.LEFT, PConstants.CENTER)
        text("analysis:", 40f, 250f)

        drawFFT(130f, 200f, 300f, 100f, sva.fft)
        drawOnset(530f, 250f, 100f, sva.beatDetect.isKick)

        // detection
        fill(255f, 238f, 173f)
        textAlign(PConstants.LEFT, PConstants.CENTER)
        text("current variance: ${sva.variance}", 40f, 350f)


        val variance = sva.varianceOverTime()
        varianceTracker.put(variance)
        text("average variance: $variance", 40f, 380f)

        var txt = ""
        when (variance) {
            in 0.00..0.015 -> txt = "low"
            in 0.015..0.03 -> txt = "mid"
            in 0.03..0.1 -> txt = "high"
        }

        if(sva.beatDetect.isKick) {
            when (txt) {
                "low" -> activeMovie = lowMovie
                "mid" -> activeMovie = midMovie
                "high" -> activeMovie = highMovie
            }
        }

        image(activeMovie, 40f, 420f, 50f, 50f)
        syphon.sendImageToSyphon(activeMovie)

        text("nervousness: $txt", 40f, 400f)

        drawBuffer(250f, 320f, 350f, 100f, varianceTracker.getLatest(varianceTracker.size()).map { x -> (x * 3f - 1f) }.toFloatArray())
    }

    internal fun drawLevel(x: Float, y: Float, w: Float, h: Float, value: Float) {
        val mappedValue = PApplet.map(value, 0f, 1f, 0f, h)

        stroke(255)
        noFill()
        rect(x, y, w, h)

        noStroke()
        fill(255f, 111f, 105f)
        rect(x + 1, y + h - mappedValue, w - 1, h - (h - mappedValue))

        textAlign(PConstants.CENTER, PConstants.CENTER)
        text("level", x + w / 2, y + h + 10f)
    }


    internal fun drawBuffer(x: Float, y: Float, w: Float, h: Float, data: FloatArray) {
        // draw border
        stroke(255)
        noFill()
        rect(x, y, w, h)

        stroke(100)
        line(x, y + (h / 2), x + w,  y + (h / 2))

        // draw data
        val step = w / data.size.toFloat()

        // draw lines
        stroke(150f, 206f, 180f)
        noFill()

        var i = 0
        while (i < data.size - 1) {
            val y1 = PApplet.map(data[i], -1f, 1f, h - 1, 0f)
            val y2 = PApplet.map(data[i + 1], -1f, 1f, h - 1, 0f)

            val x1 = step * i.toFloat()
            val x2 = step * (i + 1).toFloat()

            line(x + x1, y + y1, x + x2, y + y2)
            i += 2
        }

        fill(150f, 206f, 180f)
        textAlign(PConstants.CENTER, PConstants.CENTER)
        text("buffer", x + w / 2, y + h + 10f)
    }

    internal fun drawFFT(x: Float, y: Float, w: Float, h: Float, fft: FFT) {
        // draw border
        stroke(255)
        noFill()
        rect(x, y, w, h)

        val step = w / fft.specSize()

        // draw lines
        stroke(255f, 204f, 92f)
        noFill()

        var max = 1f
        for (i in 0..fft.specSize() - 1) {
            val value = fft.getBand(i)
            if (value > max)
                max = value
        }

        for (i in 0..fft.specSize() - 1) {
            val value = PApplet.map(fft.getBand(i), 0f, max, 0f, h)
            val ix = step * i.toFloat()

            // draw the line for frequency band i, scaling it up a bit so we can see it
            line(x + ix, y + h, x + ix, y + h - value)
        }

        textAlign(PConstants.CENTER, PConstants.CENTER)
        text("fft", x + w / 2, y + h + 10f)
    }

    internal fun drawOnset(x: Float, y: Float, r: Float, isOnset: Boolean) {
        val a = PApplet.map(onSetRadius, 20f, r, 60f, 255f)
        stroke(255)
        fill(170f, 216f, 176f, a)

        if (isOnset)
            onSetRadius = r

        ellipse(x, y, onSetRadius, onSetRadius)
        onSetRadius *= 0.95f
        if (onSetRadius < 20)
            onSetRadius = 20f
    }
}