package ch.bildspur.sva.sound

/**
 * Created by cansik on 03.10.16.
 */
class AudioUtils
{
    fun execCorrelation(x1: FloatArray, x2: FloatArray): Pair<Int, Float> {
        // define the size of the resulting correlation field
        val corrSize = 2 * x1.size
        // create correlation vector
        val out = FloatArray(corrSize)
        // shift variable
        var shift = x1.size
        var value: Float
        var maxIndex = 0
        var maxVal = 0f

        // we have push the signal from the left to the right
        for (i in 0..corrSize - 1) {
            value = 0f
            // multiply sample by sample and sum up
            for (k in x1.indices) {
                // x2 has reached his end - abort
                if (k + shift > x2.size - 1) {
                    break
                }

                // x2 has not started yet - continue
                if (k + shift < 0) {
                    continue
                }

                // multiply sample with sample and sum up
                value += x1[k] * x2[k + shift]
            }
            // save the sample
            out[i] = value
            shift--
            // save highest correlation index
            if (out[i] > maxVal) {
                maxVal = out[i]
                maxIndex = i
            }
        }

        // set the delay and confidence
        return Pair(maxIndex - x1.size, maxVal)
    }
}