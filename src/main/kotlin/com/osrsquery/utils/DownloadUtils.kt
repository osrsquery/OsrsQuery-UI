package com.osrsquery.utils

import com.google.gson.Gson
import mu.KotlinLogging

data class CacheInfo(
    val id : Int,
    val game : String,
    val timestamp : String,
    val builds : List<CacheInfoBuilds>,
    val sources : List<String>,
    val size : Long
)

data class Xteas(
    val archive : Int,
    val group : Int,
    val name_hash : Long,
    val name : String,
    val mapsquare : Int,
    val key : Array<Long>,
)

data class CacheInfoBuilds(
    var major : Int
)

private val logger = KotlinLogging.logger {}

object DownloadUtils {

    var caches : List<CacheInfo> = emptyList()

    fun generateList(data : String) {
        logger.info { "Building Cache List" }
        val caches = Gson().fromJson(data, Array<CacheInfo>::class.java).toList().filter {
            it.game.contains("oldschool")
            && it.timestamp != null && it.builds.isNotEmpty()
        }.sortedBy { it.timestamp }

        val data = emptyMap<Int,List<CacheInfo>>().toMutableMap()

        caches.forEach {

        }
        logger.info { "Cache List generated" }
    }

}