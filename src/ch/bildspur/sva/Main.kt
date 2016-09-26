package ch.bildspur.sva

import ch.bildspur.sva.sketch.SVASketch
import com.jogamp.opengl.*
import com.jogamp.opengl.awt.GLCanvas
import javafx.application.Application
import javafx.embed.swing.SwingNode
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import processing.awt.PSurfaceAWT
import processing.core.PApplet
import processing.opengl.PSurfaceJOGL
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel

/**
 * Created by cansik on 21.09.16.
 */

class Main {
    val sketch = SVASketch()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = Main()
            PApplet.runSketch(arrayOf("SVASketch "), main.sketch)
        }
    }
}