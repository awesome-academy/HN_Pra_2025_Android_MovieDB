package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.SeoOnPage
import org.json.JSONObject

fun parseSeoOnPage(json: JSONObject): SeoOnPage {
    val ogType = if (json.has("og_type")) json.optString("og_type") else null
    val titleHead = if (json.has("titleHead")) json.optString("titleHead") else null
    val descriptionHead = if (json.has("descriptionHead")) json.optString("descriptionHead") else null
    val ogImageArr = json.optJSONArray("og_image")
    val ogImage = ogImageArr?.let { arr -> List(arr.length()) { i -> arr.optString(i) } }
    val ogUrl = if (json.has("og_url")) json.optString("og_url") else null
    return SeoOnPage(ogType, titleHead, descriptionHead, ogImage, ogUrl)
}
