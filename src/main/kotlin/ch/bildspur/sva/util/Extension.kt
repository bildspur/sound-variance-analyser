package ch.bildspur.sva.util

import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage

/**
 * Created by cansik on 02.10.16.
 */

fun PApplet.center(length: Float, min: Float, max: Float): Float {
    val container = max - min
    return ((container - length) / 2f) + min
}

fun PGraphics.centerImage(img: PImage) {
    this.centerImage(img, img.width.toFloat(), img.height.toFloat())
}

fun PGraphics.centerImageAdjusted(img: PImage) {
    val scaleFactor: Float

    if (img.height > img.width)
        scaleFactor = this.height.toFloat() / img.height
    else
        scaleFactor = this.width.toFloat() / img.width

    this.centerImage(img, scaleFactor * img.width, scaleFactor * img.height)
}

fun PGraphics.centerImage(img: PImage, width: Float, height: Float) {
    this.image(img, this.width / 2.0f - width / 2.0f, this.height / 2.0f - height / 2.0f, width, height)
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)