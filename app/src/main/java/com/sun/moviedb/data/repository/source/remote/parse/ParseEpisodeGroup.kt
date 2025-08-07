package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.EpisodeGroup
import com.sun.moviedb.data.repository.source.remote.model.EpisodeItem
import org.json.JSONObject

fun parseEpisodeGroup(json: JSONObject): EpisodeGroup {
    val serverName = if (json.has("server_name")) json.optString("server_name") else null
    val serverDataArr = json.optJSONArray("server_data")
    val serverData = serverDataArr?.let { arr ->
        mutableListOf<EpisodeItem>().apply {
            for (i in 0 until arr.length()) {
                arr.optJSONObject(i)?.let { ep -> add(parseEpisodeItem(ep)) }
            }
        }
    }
    return EpisodeGroup(serverName, serverData)
}