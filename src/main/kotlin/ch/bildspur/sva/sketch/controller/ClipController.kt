package ch.bildspur.sva.sketch.controller

import ch.bildspur.event.Event
import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.model.SectorMovie
import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet
import processing.core.PGraphics

/**
 * Created by cansik on 03.10.16.
 */
class ClipController(var sketch: SVASketch, var sector: Sector) {
    val sectorChanged = Event<Pair<Sector, Sector>>()

    val output: PGraphics

    val movies = mutableListOf<SectorMovie>()

    init {
        sectorChanged += { e -> sectorChanged(e.first, e.second) }
        output = sketch.createGraphics(SVASketch.OUTPUT_WIDTH, SVASketch.OUTPUT_HEIGHT, PApplet.P2D)
    }

    fun addSector(sector: Sector) {
        movies.add(SectorMovie(sketch, sector))
    }

    fun update() {
        detectSectorChange()

        output.beginDraw()
        output.background(0)
        for (movie in movies)
            movie.render(output)
        output.endDraw()
    }

    private fun sectorChanged(last: Sector, next: Sector) {
        PApplet.println("Sector changed from ${last.name} to ${next.name}")

        getSectorMovie(last).fadeOut()
        getSectorMovie(next).fadeIn()
    }

    private fun getSectorMovie(sector: Sector): SectorMovie {
        return movies.single { it.sector == sector }
    }

    private fun detectSectorChange() {
        for (s in sketch.sectors) {
            val value = Math.min(Math.max(0f, sketch.sva.varianceOverTimeNorm()), 1f)
            if (s.inSector(value) && s != sector) {
                val last = sector
                sector = s
                sectorChanged(last, sector)
                break
            }
        }
    }
}