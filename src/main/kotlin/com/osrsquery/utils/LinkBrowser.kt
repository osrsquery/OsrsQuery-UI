package com.osrsquery.utils

import mu.KotlinLogging
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

private val logger = KotlinLogging.logger {}

object LinkBrowser {

    private var shouldAttemptXdg = OperatingSystem.isLinux()

    /**
     * Tries to navigate to specified URL in browser. In case operation fails, displays message box with message
     * and copies link to clipboard to navigate to.
     */
    fun browse(url: String) {
        Thread(Runnable {
            if (url.isEmpty()) {
                logger.warn("LinkBrowser.browse() called with invalid input")
                return@Runnable
            }
            if (shouldAttemptXdg && attemptXdgOpen(url)) {
                logger.debug("Opened url through xdg-open to {}", url)
                return@Runnable
            }
            if (attemptDesktopBrowse(url)) {
                logger.debug("Opened url through Desktop#browse to {}", url)
                return@Runnable
            }
            logger.warn("LinkBrowser.browse() could not open {}", url)
            showMessageBox("Unable to open link. Press 'OK' and the link will be copied to your clipboard.", url)
        }).start()
    }

    /**
     * Tries to open a directory in the OS native file manager.
     * @param directory directory to open
     */
    fun open(directory: String) {
        Thread(Runnable {
            if (directory.isEmpty()) {
                logger.warn("LinkBrowser.open() called with invalid input")
                return@Runnable
            }
            if (shouldAttemptXdg && attemptXdgOpen(directory)) {
                logger.debug("Opened directory through xdg-open to {}", directory)
                return@Runnable
            }
            if (attemptDesktopOpen(directory)) {
                logger.debug("Opened directory through Desktop#open to {}", directory)
                return@Runnable
            }
            logger.warn("LinkBrowser.open() could not open {}", directory)
            showMessageBox("Unable to open folder. Press 'OK' and the folder directory will be copied to your clipboard.", directory)
        }).start()
    }

    private fun attemptXdgOpen(resource: String): Boolean {
        try {
            val exec = Runtime.getRuntime().exec(arrayOf("xdg-open", resource))
            exec.waitFor()
            val ret = exec.exitValue()
            if (ret == 0) {
                return true
            }
            logger.warn("xdg-open {} returned with error code {}", resource, ret)
            return false
        } catch (ex: IOException) {
            shouldAttemptXdg = false
            return false
        } catch (ex: InterruptedException) {
            logger.warn("Interrupted while waiting for xdg-open {} to execute", resource)
            return false
        }
    }

    private fun attemptDesktopBrowse(url: String): Boolean {
        if (!Desktop.isDesktopSupported()) {
            return false
        }
        val desktop = Desktop.getDesktop()
        return if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            false
        } else try {
            desktop.browse(URI(url))
            true
        } catch (ex: IOException) {
            logger.warn("Failed to open Desktop#browse {}", url, ex)
            false
        } catch (ex: URISyntaxException) {
            logger.warn("Failed to open Desktop#browse {}", url, ex)
            false
        }
    }

    private fun attemptDesktopOpen(directory: String): Boolean {
        if (!Desktop.isDesktopSupported()) {
            return false
        }
        val desktop = Desktop.getDesktop()
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            return false
        } else try {
            desktop.open(File(directory))
            return true
        } catch (ex: IOException) {
            logger.warn("Failed to open Desktop#open {}", directory, ex)
            return false
        }
    }

    /**
     * Open swing message box with specified message and copy data to clipboard
     * @param message message to show
     */
    private fun showMessageBox(message: String, data: String) {
        SwingUtilities.invokeLater {
            val result = JOptionPane.showConfirmDialog(
                null, message, "Message",
                JOptionPane.OK_CANCEL_OPTION
            )
            if (result == JOptionPane.OK_OPTION) {
                val stringSelection = StringSelection(data)
                Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
            }
        }
    }

}