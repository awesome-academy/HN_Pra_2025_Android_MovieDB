package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieByTypeListData
import com.sun.moviedb.utils.Constants
import org.json.JSONObject

fun parseMovieByTypeListData(json: JSONObject): MovieByTypeListData {
    val seoOnPage = json.optJSONObject("seoOnPage")?.let { parseSeoOnPage(it) }
    val breadCrumbArr = json.optJSONArray("breadCrumb")
    val breadCrumb = breadCrumbArr?.let { arr -> List(arr.length()) { i -> parseBreadCrumb(arr.getJSONObject(i)) } }
    val titlePage = if (json.has("titlePage")) json.optString("titlePage") else null
    val itemsArr = json.optJSONArray("items")
    val items = itemsArr?.let { arr -> List(arr.length()) { i -> parseMovieItem(arr.getJSONObject(i), Constants.APP_DOMAIN_CDN_IMAGE) } }
    val params = json.optJSONObject("params")?.let { parseParams(it) }
    val type_list = if (json.has("type_list")) json.optString("type_list") else null
    val appDomainFrontend = if (json.has("APP_DOMAIN_FRONTEND")) json.optString("APP_DOMAIN_FRONTEND") else null
    val appDomainCdnImage = if (json.has("APP_DOMAIN_CDN_IMAGE")) json.optString("APP_DOMAIN_CDN_IMAGE") else null
    return MovieByTypeListData(
        seoOnPage, breadCrumb, titlePage, items, params, type_list, appDomainFrontend, appDomainCdnImage
    )
}