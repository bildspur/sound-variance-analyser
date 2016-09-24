package ch.bildspur.sva

import ch.bildspur.sva.sketch.SVATestSketch
import processing.core.PApplet

/**
 * Created by cansik on 21.09.16.
 */

fun main(args: Array<String>) {
    val sketch = SVATestSketch()
    PApplet.runSketch(arrayOf("SVATestSketch "), sketch)
}