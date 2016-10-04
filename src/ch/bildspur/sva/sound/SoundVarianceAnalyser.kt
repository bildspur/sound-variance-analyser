package ch.bildspur.sva.sound

import ch.bildspur.sva.sound.math.linearWeightedAverage
import ddf.minim.AudioSource
import ddf.minim.Minim
import ddf.minim.analysis.BeatDetect
import ddf.minim.analysis.FFT
import processing.core.PApplet

/**
 * Created by cansik on 21.09.16.
 */

class SoundVarianceAnalyser(internal var sketch: PApplet) {
    val BUFFER_SIZE = 2048
    val RING_BUFFER_SIZE = BUFFER_SIZE * 10
    val MAX_SENSITIVTY = 300

    var maxVariance = 0.01f

    var minVariance = 0.0f

    var sensitivity = 0.5f

    var minim: Minim
        internal set

    var source: AudioSource
        internal set

    var track: LoopRingBuffer
        internal set

    var variance: Float = 0f
        internal set

    var varianceTracking: LoopRingBuffer = LoopRingBuffer(MAX_SENSITIVTY)

    init {
        minim = Minim(sketch)

        source = minim.lineIn
        track = LoopRingBuffer(1)
    }

    fun init() {
        this.init(minim.getLineIn(Minim.MONO, BUFFER_SIZE))
    }

    fun init(source: AudioSource) {
        this.source = source
        track = LoopRingBuffer(RING_BUFFER_SIZE)
    }

    fun varianceOverTimeNorm() : Float {
        return normalizeVariance(varianceOverTime())
    }

    fun varianceOverTime() : Float {
        val sensitivitySize = (MAX_SENSITIVTY * (1f - sensitivity)).toInt()
        val values = varianceTracking.getLatest(sensitivitySize)
        return values.linearWeightedAverage().toFloat()
    }

    internal fun normalizeVariance(variance:Float) : Float {
        return PApplet.map(variance, minVariance, maxVariance, 0f, 1f)
    }

    fun listen() {
        track.put(source.mix.toArray())

        // detect variance
        variance = 0f
        var lastValue = 0f

        for(i in source.mix.toArray().indices)
        {
            variance += Math.abs(lastValue - source.mix[i]).toFloat()
            lastValue = source.mix[i]
        }

        variance /= source.mix.size()
        varianceTracking.put(variance)
    }
}