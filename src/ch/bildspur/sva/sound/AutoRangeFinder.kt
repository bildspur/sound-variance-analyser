package ch.bildspur.sva.sound

import ch.bildspur.event.Event
import ch.bildspur.sva.model.Sector

/**
 * Created by cansik on 03.10.16.
 */
class AutoRangeFinder() {

    var minValue: Float = 0f
    var maxValue: Float = 0f

    val newRangeFound = Event<AutoRangeFinder>()

    val history = LoopRingBuffer(900)

    fun update(value :Float)
    {
        history.put(value)

        val buffer = history.getBuffer()
        val min = buffer.min()!!.toFloat()
        val max = buffer.max()!!.toFloat()

        if(min != minValue)
        {
            minValue = min
            newRangeFound(this)
        }

        if(max != maxValue)
        {
            maxValue = max
            newRangeFound(this)
        }
    }

    fun reset()
    {

    }
}