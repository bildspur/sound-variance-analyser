package ch.bildspur.sva.sound

import ch.bildspur.event.Event
import ch.bildspur.sva.model.Sector

/**
 * Created by cansik on 03.10.16.
 */
class AutoRangeFinder() {

    var minValue: Float = 0f
    var maxValue: Float = 0f

    private var afterReset = true

    val newRangeFound = Event<AutoRangeFinder>()

    fun update(value :Float)
    {
        if(afterReset)
        {
            minValue = value
            maxValue = value
            afterReset = false

            newRangeFound(this)

            return
        }

        if(minValue > value) {
            minValue = value
            newRangeFound(this)
        }

        if(maxValue < value) {
            maxValue = value
            newRangeFound(this)
        }
    }

    fun reset()
    {
        afterReset = true
    }
}