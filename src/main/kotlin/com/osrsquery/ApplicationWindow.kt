package com.osrsquery


import com.osrsquery.utils.setIcon
import com.osrsquery.utils.setStyle
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.File

class ApplicationWindow : Application() {

    companion object {
        lateinit var stage: Stage
        lateinit var scene: Scene
    }

    override fun start(primaryStage: Stage) {
        System.setProperty("prism.lcdtext", "false")
        System.setProperty("prism.text", "t2k")
        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource("views/Application.fxml"))


        scene = Scene(root)
        primaryStage.initStyle(StageStyle.TRANSPARENT)
        scene.setStyle()
        primaryStage.setIcon()
        primaryStage.scene = scene
        primaryStage.show()
        primaryStage.centerOnScreen()

        scene.onMousePressed = EventHandler { pressEvent: MouseEvent ->
            scene.onMouseDragged = EventHandler { dragEvent: MouseEvent ->
                primaryStage.x = dragEvent.screenX - pressEvent.sceneX
                primaryStage.y = dragEvent.screenY - pressEvent.sceneY
            }
        }
        stage = primaryStage
        stage.toFront()
    }
}

val MAIN_DIR = File(System.getProperty("user.home"), ".osrsquery")
val LOGS_DIR = File(MAIN_DIR, "logs")

fun main() {

    Application.launch(ApplicationWindow::class.java)

}
