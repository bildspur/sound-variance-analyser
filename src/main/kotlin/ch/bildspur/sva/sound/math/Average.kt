package ch.bildspur.sva.sound.math

/**
 * Created by cansik on 03.10.16.
 */

fun FloatArray.linearWeightedAverage(): Double {
    // gauss
    val n = (this.size + 1).toDouble()
    val divider = (Math.pow(n, 2.0) + n) / 2.0

    // average
    var sum = 0.0
    for ((index, value) in this.withIndex())
        sum += ((index + 1).toDouble() / divider) * value

    return sum
}