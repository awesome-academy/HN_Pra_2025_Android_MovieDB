package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.EpisodeGroup
import com.sun.moviedb.data.repository.source.remote.model.EpisodeItem
import org.json.JSONObject

fun parseEpisodeGroup(json: JSONObject): EpisodeGroup {
    val serverName = json.optString("server_name")
    val serverDataArr = json.optJSONArray("server_data")
    val serverData = mutableListOf<EpisodeItem>()
    if (serverDataArr != null) {
        for (i in 0 until serverDataArr.length()) {
            val ep = serverDataArr.optJSONObject(i)
            if (ep != null) serverData.add(parseEpisodeItem(ep))
        }
    }
    return EpisodeGroup(serverName, serverData)
}