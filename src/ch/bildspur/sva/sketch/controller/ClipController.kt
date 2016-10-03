package ch.bildspur.sva.sketch.controller

import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.sketch.SVASketch
import processing.core.PApplet

/**
 * Created by cansik on 03.10.16.
 */
class ClipController(var sketch: SVASketch, var sector: Sector)
{
    fun update()
    {
        detectSectorChange()
    }

    private fun detectSectorChange()
    {
        for (s in sketch.sectors)
        {
            val value = Math.min(Math.max(0f, sketch.sva.varianceOverTimeNorm()), 1f)
            if(s.inSector(value) && s != sector) {
                PApplet.println("Sector changed to: ${s.name}")
                sector = s
                break
            }
        }
    }
}