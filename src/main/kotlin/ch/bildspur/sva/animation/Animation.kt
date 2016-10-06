package ch.bildspur.sva.animation

import ch.bildspur.event.Event
import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet

/**
 * Created by cansik on 04.10.16.
 */
class Animation(var time: Float, var start: Float, var end: Float) {

    val zero = 0.0001f

    var value: Float = 0f
    var frameTime: Float = 0f

    var animationTimeCount: Int = 0

    var incValue: Float = 0f

    var running: Boolean = false

    var animationStopped = Event<Animation>()

    init {
        value = start
        setTimeInSeconds(time)
    }

    fun update() {
        if (!running)
            return

        value += incValue
        animationTimeCount++

        if (animationTimeCount >= frameTime || end <= value)
            stop()
    }

    fun start() {
        incValue = 1f / frameTime * (PApplet.max(zero, end - start))
        running = true
        value = start
        animationTimeCount = 0
    }

    fun reverse() {
        val temp = start
        this.start = end
        this.end = temp
    }

    fun stop() {
        running = false
        animationTimeCount = 0
        value = end
        animationStopped(this)
    }

    fun setTimeInSeconds(time: Float) {
        frameTime = secondsToFrames(PApplet.max(zero, time)).toFloat()
    }

    private fun secondsToFrames(seconds: Float): Int {
        return (seconds * SVASketch.FRAME_RATE).toInt()
    }
}