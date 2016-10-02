package ch.bildspur.sva.model

/**
 * Created by cansik on 02.10.16.
 */
class Sector (var name:String, var start:Float, var end:Float) {
    var fadeIn = 0f
    var fadeOut = 0f

    var clipDuration = 100

    val length: Float
        get() = end - start

    fun inSector(value:Float) : Boolean {
        return (start <= value && value < end)
    }
}