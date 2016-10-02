package ch.bildspur.sva.ui

import ch.bildspur.sva.sketch.SVASketch
import processing.core.PGraphics
import processing.core.PVector

/**
 * Created by cansik on 02.10.16.
 */
class ClipView(val sketch: SVASketch, val width:Float, val height:Float) {
    var position = PVector()
    var image : PGraphics? = null

    fun render() {
        sketch.translate(position.x, position.y)

        if(image != null)
            sketch.image(image, 0f, 0f, width, height)

        drawBorder()
        sketch.translate(-position.x, -position.y)
    }

    internal fun drawBorder()
    {
        sketch.stroke(255)
        sketch.strokeWeight(1f)
        sketch.noFill()
        sketch.rect(0f, 0f, width, height)
    }
}