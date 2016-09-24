package ch.bildspur.sva

import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet

/**
 * Created by cansik on 21.09.16.
 */

fun main(args: Array<String>) {
    val sketch = SVASketch()
    PApplet.runSketch(arrayOf("SVASketch "), sketch)
}