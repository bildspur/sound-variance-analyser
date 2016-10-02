package ch.bildspur.sva.ui

import ch.bildspur.sva.sketch.SVASketch
import processing.core.PGraphics

/**
 * Created by cansik on 02.10.16.
 */
class LineView(val sketch: SVASketch, val height:Float, val margin:Float) {
        fun render(g: PGraphics)
        {
            g.stroke(255)
            g.strokeWeight(1f)
            g.noFill()

            g.line(margin, height, sketch.width - margin, height)
        }
}