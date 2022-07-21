package com.osrsquery.controllers


import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.osrsquery.ApplicationWindow
import com.osrsquery.utils.DownloadUtils
import com.osrsquery.utils.DownloadUtils.caches
import com.osrsquery.utils.ErrorDialog
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import mu.KotlinLogging
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

class ApplicationController : Initializable {

    @FXML lateinit var min: Button
    @FXML lateinit var exit: Button
    @FXML lateinit var loadingPane: AnchorPane
    @FXML lateinit var downloadPane: AnchorPane
    @FXML lateinit var statusText: Label
    @FXML lateinit var all: Button
    @FXML lateinit var current: Button
    @FXML lateinit var scroll: ScrollPane
    @FXML lateinit var scrollContentPane: Pane



    override fun initialize(location: URL?, resources: ResourceBundle?) {
        scroll.hbarPolicyProperty().value = ScrollPane.ScrollBarPolicy.NEVER
        scroll.vbarPolicyProperty().value = ScrollPane.ScrollBarPolicy.ALWAYS

        min.text = "\uf068"
        exit.text = "\uf00d"
        downloadPane.isVisible = false
        loadingPane.isVisible = true

        Platform.runLater {
            statusText.text = "Connecting to OpenRs2"
            logger.info { "Connecting to OpenRs2" }
            "https://archive.openrs2.org/caches.json".httpGet().responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        ErrorDialog.showRs2(
                            "Error Loading OpenRs2",
                            "Unable to load Caches from OpenRs2",
                            result.getException().toString()
                        )
                    }
                    is Result.Success -> {
                        Platform.runLater {
                            statusText.text = "Generating OpenRs2 List"
                        }
                        logger.info { "Generating OpenRs2 List" }
                        val data = result.get()
                        DownloadUtils.generateList(data)
                        handleDownloadPane()
                    }
                }
            }
        }

        logger.info { "Main Application Initialized" }

    }

    @FXML private fun buttonHandler(event: ActionEvent) {
        val button = event.source as Button
        when (button.text) {
            "\uF068" -> ApplicationWindow.stage.isIconified = true
            "\uF00D" ->  {
                logger.info { "Application Ending" }
                Platform.exit()
                exitProcess(0)
            }
        }
    }

    @FXML private fun buttonClick(event: ActionEvent) {
        val button = event.source as Button
        when (button.id) {
            "current" -> {

            }
            "all" ->  {
                logger.info { "Application Ending" }
                Platform.exit()
                exitProcess(0)
            }
        }
    }

    private fun handleDownloadPane() {
        Platform.runLater {

            downloadPane.isVisible = true
            loadingPane.isVisible = false

            all.text = "OSRS (${caches.size})"
            current.text = "Downloaded (${2})"

            val xOffset = 0
            var yOffset = 0

            caches.forEach {
                var sidebarName = Label("dfdf")
                sidebarName.layoutX = xOffset.toDouble()
                sidebarName.layoutY = yOffset.toDouble()

                sidebarName.style = "-fx-font-size: 15pt;"
                scrollContentPane.children.add(sidebarName)
                yOffset += 10
            }
        }
    }

}