package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.EpisodeItem
import org.json.JSONObject

fun parseEpisodeItem(json: JSONObject): EpisodeItem {
    return EpisodeItem(
        name = if (json.has("name")) json.optString("name") else null,
        slug = if (json.has("slug")) json.optString("slug") else null,
        filename = if (json.has("filename")) json.optString("filename") else null,
        link_embed = if (json.has("link_embed")) json.optString("link_embed") else null,
        link_m3u8 = if (json.has("link_m3u8")) json.optString("link_m3u8") else null
    )
}