/*
 * Copyright (c) 2022, Mark <https://github.com/Mark7625>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.osrsquery.utils

import com.osrsquery.utils.AssetsUtils.getResource
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import mu.KotlinLogging
import java.awt.Taskbar
import javax.swing.ImageIcon

private val logger = KotlinLogging.logger {}

fun Stage.setIcon() {
    val fav : List<Image> = listOf(
        Image(getResource("16x16.png").toString()),
        Image(getResource("32x32.png").toString()),
        Image(getResource("48x48.png").toString()),
        Image(getResource("64x64.png").toString()),
        Image(getResource("128x128.png").toString()),
        Image(getResource("256x256.png").toString())
    )
    if(OperatingSystem.isWindows()) {
        this.icons.addAll(fav)
    } else if(OperatingSystem.isMac()) {
        val taskbar = Taskbar.getTaskbar()
        taskbar.iconImage = ImageIcon(getResource("1024x1024.png")).image
    }
    logger.info { "Loaded App Icons" }
}

fun Scene.setStyle() {
    stylesheets.clear()
    stylesheets.add("dark.css")
    logger.info { "Loaded Stylesheet" }
}