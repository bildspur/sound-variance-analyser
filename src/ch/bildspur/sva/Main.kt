package ch.bildspur.sva

import ch.bildspur.sva.controller.MainWindowController
import ch.bildspur.sva.sketch.SVASketch
import com.sun.org.apache.xerces.internal.dom.ParentNode
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import processing.core.PApplet

/**
 * Created by cansik on 21.09.16.
 */

class Main : Application() {
    val sketch = SVASketch()

    override fun start(stage: Stage) {
        PApplet.runSketch(arrayOf("SVASketch "), sketch)

        val root = FXMLLoader.load<Parent>(javaClass.getResource("views/MainWindow.fxml"))
        val scene = Scene(root, 300.0, 275.0)

        stage.title = "Hello World"
        stage.scene = scene
        stage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}