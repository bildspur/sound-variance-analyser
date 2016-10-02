package ch.bildspur.sva.util

import processing.core.PApplet

/**
 * Created by cansik on 02.10.16.
 */

fun PApplet.center(length:Float, min:Float, max:Float) : Float
{
    val container = max - min
    return ((container - length) / 2f) + min
}