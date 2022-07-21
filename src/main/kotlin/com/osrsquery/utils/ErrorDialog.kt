package com.osrsquery.utils

import com.osrsquery.ApplicationWindow
import com.osrsquery.LOGS_DIR
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*
import javax.swing.border.EmptyBorder
import kotlin.system.exitProcess


class ErrorDialog(message: String) : JDialog() {

    val DARKER_GRAY_COLOR = Color(30, 30, 30)
    val DARK_GRAY_COLOR = Color(40, 40, 40)
    val DARK_GRAY_HOVER_COLOR = Color(35, 35, 35)

    private val rightColumn = JPanel()
    private val fontError = Font(Font.DIALOG, Font.PLAIN, 12)
    private val title: JLabel

    init {
        if (alreadyOpen.getAndSet(true)) {
            throw IllegalStateException("Fatal error during fatal error: $message")
        }
        try {
            val logo = toolkit.getImage(ApplicationWindow::class.java.getResource("/128x128.png"))
            setIconImage(logo)
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                exitProcess(-1)
            }
        })

        layout = BorderLayout()
        val pane = contentPane as JPanel
        pane.background = DARKER_GRAY_COLOR
        val leftPane = JPanel()
        leftPane.background = DARKER_GRAY_COLOR
        leftPane.layout = BorderLayout()
        title = JLabel("There was a fatal error starting OsrsQuery")
        title.foreground = Color.WHITE
        title.font = fontError.deriveFont(16f)
        title.border = EmptyBorder(10, 10, 10, 10)
        leftPane.add(title, BorderLayout.NORTH)
        leftPane.preferredSize = Dimension(400, 200)
        val textArea = JTextArea(message)
        textArea.font = fontError
        textArea.background = DARKER_GRAY_COLOR
        textArea.foreground = Color.LIGHT_GRAY
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        textArea.border = EmptyBorder(10, 10, 10, 10)
        textArea.isEditable = false
        textArea.isOpaque = false
        leftPane.add(textArea, BorderLayout.CENTER)
        pane.add(leftPane, BorderLayout.CENTER)
        rightColumn.layout = BoxLayout(rightColumn, BoxLayout.Y_AXIS)
        rightColumn.background = DARK_GRAY_COLOR
        rightColumn.maximumSize = Dimension(200, Int.MAX_VALUE)
        pane.add(rightColumn, BorderLayout.EAST)
    }

    fun open() {
        addButton("Exit") { exitProcess(-1) }
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun addButton(message: String, action: Runnable): ErrorDialog {
        val button = JButton(message)
        button.addActionListener { action.run() }
        button.font = font
        button.background = DARK_GRAY_COLOR
        button.foreground = Color.LIGHT_GRAY
        button.border = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, DARK_GRAY_COLOR.brighter()),
            EmptyBorder(4, 4, 4, 4)
        )
        button.alignmentX = CENTER_ALIGNMENT
        button.maximumSize = Dimension(Int.MAX_VALUE, Int.MAX_VALUE)
        button.isFocusPainted = false
        button.addChangeListener {
            when {
                button.model.isPressed -> button.background = DARKER_GRAY_COLOR
                button.model.isRollover -> button.background = DARK_GRAY_HOVER_COLOR
                else -> button.background = DARK_GRAY_COLOR
            }
        }

        rightColumn.add(button)
        rightColumn.revalidate()
        return this
    }

    fun setTitle(windowTitle: String?, header: String?): ErrorDialog {
        super.setTitle(windowTitle)
        title.text = header
        return this
    }

    fun addHelpButtons(): ErrorDialog {
        return addButton("Open logs folder") { LinkBrowser.open(LOGS_DIR.toString()) }
            .addButton("Get help on Discord") { LinkBrowser.browse("https://discord.gg/QQFkVwcvS5") }
            .addButton("Troubleshooting steps") { LinkBrowser.browse("https://discord.gg/QQFkVwcvS5") }
    }


    companion object {

        private val alreadyOpen = AtomicBoolean(false)

        fun showCustom(title : String = "OsrsQuery - Error",error : String = "Fatal Error", exception: String) {
            ErrorDialog(
                exception
            ).setTitle("OsrsQuery - $title",error)
                .addHelpButtons()
            .open()
        }

        fun showRs2(title : String = "OsrsQuery - Error",error : String = "Fatal Error", exception: String) {
            ErrorDialog(
                exception
            ).setTitle("OsrsQuery - $title",error)
                .addHelpButtons()
                .addButton("Get help on OpenRs2 Discord") { LinkBrowser.browse("https://discord.gg/gBCssh7zMb") }
                .addButton("OpenRs2 Website") { LinkBrowser.browse("https://archive.openrs2.org/") }
            .open()
        }

    }
}