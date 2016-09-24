package ch.bildspur.sva.sound

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

    var minim: Minim
        internal set

    var source: AudioSource
        internal set

    var track: LoopRingBuffer
        internal set

    var fft: FFT
        internal set

    var beatDetect: BeatDetect
        internal set

    var variance: Float = 0f
        internal set

    var varianceTracking: LoopRingBuffer = LoopRingBuffer(100)

    fun varianceOverTime() : Float
    {
        return varianceTracking.getBuffer().average().toFloat()
    }

    init {
        minim = Minim(sketch)

        source = minim.lineIn
        track = LoopRingBuffer(0)
        fft = FFT(1, 1f)
        beatDetect = BeatDetect()
    }

    fun init() {
        this.init(minim.getLineIn(Minim.MONO, BUFFER_SIZE))
    }

    fun init(source: AudioSource) {
        this.source = source
        track = LoopRingBuffer(RING_BUFFER_SIZE)

        fft = FFT(source.bufferSize(), source.sampleRate())
        beatDetect = BeatDetect(source.bufferSize(), source.sampleRate())
        beatDetect.setSensitivity(200)
    }

    fun listen() {
        track.put(source.mix.toArray())

        fft.forward(source.mix)
        beatDetect.detect(source.mix)

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