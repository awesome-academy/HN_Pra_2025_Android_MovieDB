package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.TmdbInfo
import org.json.JSONObject

fun parseTmdbInfo(json: JSONObject): TmdbInfo {
    return TmdbInfo(
        type = json.optString("type"),
        id = json.optString("id"),
        season = if (json.has("season")) json.optInt("season") else null,
        vote_average = if (json.has("vote_average")) json.optDouble("vote_average") else null,
        vote_count = if (json.has("vote_count")) json.optInt("vote_count") else null
    )
}
