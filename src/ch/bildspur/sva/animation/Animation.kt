package ch.bildspur.sva.animation

import ch.bildspur.event.Event
import ch.bildspur.sva.sketch.SVASketch

/**
 * Created by cansik on 04.10.16.
 */
class Animation(var time: Float, var start: Float, var end: Float) {
    var value: Float = 0f
    var frameTime: Float = 0f

    var animationTimeCount: Int = 0

    var incValue: Float = 0f

    var running: Boolean = false

    var animationStopped = Event<Animation>()

    init {
        value = start
        frameTime = secondsToFrames(time).toFloat()
    }

    fun update() {
        if (!running)
            return

        value += incValue
        animationTimeCount++

        if (animationTimeCount >= frameTime)
            stop()
    }

    fun start() {
        incValue = 1f / frameTime * (end - start)
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

        animationStopped(this)
    }

    private fun secondsToFrames(seconds: Float): Int {
        return (seconds * SVASketch.FRAME_RATE).toInt()
    }
}