package ch.bildspur.sva.sketch.controller

import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.sketch.SVASketch
import ch.bildspur.sva.ui.ClipView
import ch.bildspur.sva.ui.LineView
import ch.bildspur.sva.ui.SectorBar
import ch.bildspur.sva.util.center
import controlP5.ControlFont
import controlP5.ControlP5
import controlP5.Slider
import controlP5.Textfield
import processing.core.PApplet
import processing.core.PVector
import java.awt.Color
import kotlin.properties.Delegates

/**
 * Created by cansik on 25.09.16.
 */
class UIController(val sketch: SVASketch)
{
    val controlSpace = 10f
    val margin = 25f
    val labelMarginLeft = -80
    val labelMarginTop = -19
    val editControlX = (sketch.width / 2) + margin + (-labelMarginLeft)
    val editControlWidth = 150
    val editControlHeight = 20

    val defaultFont = "Lucida Sans"
    val defaultFontSize = 9f

    val backgroundColor = Color(52, 73, 94)

    val cp5:ControlP5
    val sectorView = SectorBar(sketch, sketch.width - (2f * margin), 40f)

    var nameField: Textfield by Delegates.notNull()
    var startField : Textfield by Delegates.notNull()
    var endField : Textfield by Delegates.notNull()

    var fadeInSlider : Slider by Delegates.notNull()
    var fadeOutSlider : Slider by Delegates.notNull()

    var clipDurationSlider: Slider by Delegates.notNull()
    var clipFolderField: Textfield by Delegates.notNull()

    var selectedSector : Sector? = null

    var clipView : ClipView by Delegates.notNull()

    var separator : LineView by Delegates.notNull()

    var sensitivitySlider: Slider by Delegates.notNull()

    init {
        cp5 = ControlP5(sketch)
    }

    fun init() {
        var hPos = controlSpace

        cp5.isAutoDraw = false

        sectorView.position = PVector(center(sectorView.width), hPos)
        sectorView.sectorSelected += { sectorSelected(it) }
        hPos += sectorView.height + (2 * controlSpace)

        // test
        sectorView.addSector(Sector("Low", 0f, 0.5f, "low"))
        sectorView.addSector(Sector("High", 0.5f, 1f, "high"))

        clipView = ClipView(sketch, 200f, 200f)
        clipView.position = PVector(sketch.center(clipView.width, sectorView.position.x, sketch.width / 2f), hPos)

        // init cp5 controls
        nameField = cp5.addTextfield("Name")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .setAutoClear(false)
                .onChange { e ->
                    selectedSector?.name = nameField.text
                }
        hPos += nameField.height + controlSpace

        startField = cp5.addTextfield("Start")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .setAutoClear(false)
                .onChange { e ->
                    val result = tryParseFloat(startField.text)
                    if(result.first)
                        selectedSector?.start = result.second
                }
        hPos += startField.height + controlSpace

        endField = cp5.addTextfield("End")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .setAutoClear(false)
                .onChange { e ->
                    val result = tryParseFloat(endField.text)
                    if(result.first)
                        selectedSector?.end = result.second
                }
        hPos += endField.height + controlSpace

        fadeInSlider = cp5.addSlider("Fade In")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .onChange { e ->
                    selectedSector?.fadeIn = fadeInSlider.value
                }
        hPos += endField.height + controlSpace

        fadeOutSlider = cp5.addSlider("Fade Out")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .onChange { e ->
                    selectedSector?.fadeOut = fadeOutSlider.value
                }
        hPos += endField.height + controlSpace

        clipDurationSlider = cp5.addSlider("Clip Duration")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .onChange { e ->
                    selectedSector?.clipDuration = clipDurationSlider.value.toInt()
                }
        hPos += endField.height + controlSpace

        clipFolderField = cp5.addTextfield("Clip Folder")
                .setPosition(editControlX, hPos)
                .setSize(editControlWidth, editControlHeight)
                .setAutoClear(false)
                .onChange { e ->
                    selectedSector?.clipFolder = clipFolderField.text
                }
        hPos += endField.height + (2 * controlSpace)

        separator = LineView(sketch, hPos, 2 * margin)

        hPos += (2 * controlSpace)

        sensitivitySlider = cp5.addSlider("Sensitivity")
                .setPosition(margin * 2, hPos)
                .setSize(editControlWidth, editControlHeight)
                .setValue(sketch.sva.sensitivity)
                .setRange(0f, 1f)
                .onChange { e ->
                    sketch.sva.sensitivity = sensitivitySlider.value
                }

        styleCP5()
    }

    internal fun selectedSectorChanged()
    {
        if(selectedSector == null)
            return

        nameField.text = selectedSector!!.name
        startField.text = selectedSector!!.start.toString()
        endField.text = selectedSector!!.end.toString()

        fadeInSlider.value = selectedSector!!.fadeIn
        fadeOutSlider.value = selectedSector!!.fadeOut

        clipDurationSlider.value = selectedSector!!.clipDuration.toFloat()
        clipFolderField.text = selectedSector!!.clipFolder
    }

    internal fun clearFields()
    {
        nameField.text = ""
        startField.text = ""
        endField.text = ""

        fadeInSlider.value = 0f
        fadeOutSlider.value = 0f

        clipDurationSlider.value = 0f
        clipFolderField.text = ""
    }

    internal fun styleCP5()
    {
        val pfont = sketch.createFont(defaultFont, defaultFontSize, false)
        val font = ControlFont(pfont, defaultFontSize.toInt())

        for(control in cp5.all)
        {
            control.setFont(font)
            control.setColorBackground(backgroundColor.rgb)

            when (control)
            {
                is Textfield -> {
                    control.captionLabel.style.marginTop = labelMarginTop
                    control.captionLabel.style.marginLeft = labelMarginLeft
                    control.valueLabel.font = font
                    control.valueLabel.setSize(defaultFontSize.toInt())
                    control.valueLabel.toUpperCase(false)

                    control.setColorCursor(Color(255, 255, 255).rgb)
                }

                is Slider -> {
                    control.captionLabel.style.marginLeft = -editControlWidth + labelMarginLeft - 5
                }
            }
        }
    }

    fun sectorSelected(sector:Sector)
    {
        selectedSector = sector
        selectedSectorChanged()
    }

    fun render()
    {
        cp5.begin()
        cp5.draw()
        cp5.end()

        sectorView.render()
        clipView.render()
        separator.render(sketch.g)
    }

    internal fun center(width:Float):Float
    {
        return sketch.center(width, 0f, sketch.width.toFloat())
    }

    internal fun isOverControl(v: PVector): Boolean {
        return v.x >= sectorView.position.x && v.x < sectorView.position.x + sectorView.width
                && v.y >= sectorView.position.y && v.y < sectorView.position.y + sectorView.height
    }

    internal fun tryParseFloat(text: String): Pair<Boolean, Float> {
        var parsed = false
        var value = 0.0f

        try {
            value = java.lang.Float.parseFloat(text)
            parsed = true
        } catch (ex: Exception) {
        }

        return Pair(parsed, value)
    }

    fun mousePressed() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        if (isOverControl(mouse))
            sectorView.mousePressed(PVector.sub(mouse, sectorView.position))
    }

    fun mouseDragged() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        if (isOverControl(mouse))
            sectorView.mouseDragged(PVector.sub(mouse, sectorView.position))
    }

    fun mouseMoved() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        sectorView.mouseMoved(PVector.sub(mouse, sectorView.position))
    }

    fun mouseReleased() {
        val mouse = PVector(sketch.mouseX.toFloat(), sketch.mouseY.toFloat())
        if (isOverControl(mouse))
            sectorView.mouseReleased(PVector.sub(mouse, sectorView.position))
    }
}