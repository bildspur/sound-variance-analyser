package ch.bildspur.sva.model

import ch.bildspur.sva.animation.Animation
import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet
import processing.core.PGraphics
import processing.video.Movie
import java.io.File

/**
 * Created by cansik on 04.10.16.
 */
class SectorMovie(val sketch: SVASketch, val sector: Sector) {
    var movie: Movie? = null

    var alpha = 0f

    var movieTimer = 0

    var fadeAnimation = Animation(1f, 0f, 255f)

    init {
        loadNextMovie()
        fadeAnimation.start()
    }

    fun render(g: PGraphics) {
        if (movie != null) {
            g.tint(255, alpha)
            g.image(movie!!, 0f, 0f, g.width.toFloat(), g.height.toFloat())
        }

        fadeAnimation.update()
        alpha = fadeAnimation.value

        movieTimer++
    }

    internal fun loadNextMovie() {
        // select first movie from folder
        val folder = File(sector.clipFolder)
        val moviePath = folder.list().filter { it.toLowerCase().endsWith(".mov") }.first()
        movie = Movie(sketch, "$folder/$moviePath")
        movie!!.loop()

        PApplet.println("Loaded for ${sector.name}: $moviePath")
    }

    fun fadeOut() {
        fadeAnimation.setTimeInSeconds(sector.fadeOut) // * PApplet.map(alpha, 0f, 255f, 0f, 1f)
        fadeAnimation.start = 255f
        fadeAnimation.end = 0f
        fadeAnimation.start()
    }

    fun fadeIn() {
        fadeAnimation.setTimeInSeconds(sector.fadeIn) // * PApplet.map(alpha, 255f, 0f, 0f, 1f)
        fadeAnimation.start = 0f
        fadeAnimation.end = 255f
        fadeAnimation.start()
    }
}