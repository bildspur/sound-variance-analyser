package ch.bildspur.sva.util

import processing.core.PVector

/**
 * Created by cansik on 02.10.16.
 */
class Rectangle(val x :Float, val y:Float, val width:Float, val height:Float) {
    val position : PVector
        get() = PVector(x, y)

    fun isInside(m : PVector) : Boolean
    {
        return (m.x >= x && m.y >= y) && (m.x <= (x + width) && m.y <= (y + height))
    }

    override fun toString(): String {
        return "Rect ($x, $y, $width, $height)"
    }
}