package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieByTypeListData
import com.sun.moviedb.utils.Constants
import org.json.JSONObject

fun parseMovieByTypeListData(json: JSONObject): MovieByTypeListData {
    val seoOnPage = json.optJSONObject("seoOnPage")?.let { parseSeoOnPage(it) }
    val breadCrumbArr = json.optJSONArray("breadCrumb")
    val breadCrumb = breadCrumbArr?.let { arr -> List(arr.length()) { i -> parseBreadCrumb(arr.getJSONObject(i)) } }
    val titlePage = json.optString("titlePage")
    val itemsArr = json.optJSONArray("items")
    val items = itemsArr?.let { arr -> List(arr.length()) { i -> parseMovieItem(arr.getJSONObject(i), Constants.APP_DOMAIN_CDN_IMAGE) } }
    val params = json.optJSONObject("params")?.let { parseParams(it) }
    val type_list = json.optString("type_list")
    val appDomainFrontend = json.optString("APP_DOMAIN_FRONTEND")
    val appDomainCdnImage = json.optString("APP_DOMAIN_CDN_IMAGE")
    return MovieByTypeListData(
        seoOnPage, breadCrumb, titlePage, items, params, type_list, appDomainFrontend, appDomainCdnImage
    )
}