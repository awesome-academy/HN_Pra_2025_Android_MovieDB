package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.EpisodeItem
import org.json.JSONObject

fun parseEpisodeItem(json: JSONObject): EpisodeItem {
    return EpisodeItem(
        name = json.optString("name"),
        slug = json.optString("slug"),
        filename = json.optString("filename"),
        link_embed = json.optString("link_embed"),
        link_m3u8 = json.optString("link_m3u8")
    )
}