package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.SeoOnPage
import org.json.JSONObject

fun parseSeoOnPage(json: JSONObject): SeoOnPage {
    val ogType = json.optString("og_type")
    val titleHead = json.optString("titleHead")
    val descriptionHead = json.optString("descriptionHead")
    val ogImageArr = json.optJSONArray("og_image")
    val ogImage = ogImageArr?.let { arr -> List(arr.length()) { i -> arr.optString(i) } }
    val ogUrl = json.optString("og_url")
    return SeoOnPage(ogType, titleHead, descriptionHead, ogImage, ogUrl)
}


