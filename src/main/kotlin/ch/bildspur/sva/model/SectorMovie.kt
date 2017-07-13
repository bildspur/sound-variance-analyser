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
    companion object
    {
        @JvmStatic val MOVIE_DISPLAY_TIME = 1000 * 20
    }

    lateinit var movie: Movie

    val movies = mutableListOf<Movie>()

    var alpha = 0f

    var lastMovieChange = 0

    var movieIndex = 0

    var fadeAnimation = Animation(1f, 0f, 255f)

    var firstInit = true

    init {
        loadMovies()
        playNextMovie()
        fadeAnimation.start()

        firstInit = false
    }

    fun render(g: PGraphics) {
        g.tint(255, alpha)
        g.image(movie, 0f, 0f, g.width.toFloat(), g.height.toFloat())

        fadeAnimation.update()
        alpha = fadeAnimation.value

        if(sketch.millis() - lastMovieChange > MOVIE_DISPLAY_TIME)
            playNextMovie()
    }

    internal fun playNextMovie()
    {
        val oldMovieIndex = movieIndex
        movieIndex = (movieIndex + 1) % movies.size

        if(oldMovieIndex != movieIndex) {
            if(!firstInit)
                movie.stop()

            movie = movies[movieIndex]
            movie.loop()
        }

        lastMovieChange = sketch.millis()
    }

    internal fun loadMovies() {
        // select next movie from folder
        val folder = File(sector.clipFolder)
        val moviePaths = folder.list().filter { it.toLowerCase().endsWith(".mov") }

        moviePaths.forEach{
            val fileName = "$folder/$it"
            val m = Movie(sketch, fileName)

            if(!movies.any { it.filename ==  fileName}) {
                movies.add(m)
                PApplet.println("Loaded for ${sector.name}: $it")
            }
        }
    }

    fun fadeOut() {
        fadeAnimation.setTimeInSeconds(sector.fadeOut * PApplet.map(alpha, 0f, 255f, 0f, 1f))
        fadeAnimation.start = alpha
        fadeAnimation.end = 0f
        fadeAnimation.start()
    }

    fun fadeIn() {
        fadeAnimation.setTimeInSeconds(sector.fadeIn * PApplet.map(alpha, 255f, 0f, 0f, 1f))
        fadeAnimation.start = alpha
        fadeAnimation.end = 255f
        fadeAnimation.start()
    }
}