package ch.bildspur.sva

import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet

/**
 * Created by cansik on 21.09.16.
 */

class Main {
    val sketch = SVASketch()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = Main()
            PApplet.runSketch(arrayOf("SVASketch "), main.sketch)
        }
    }
}