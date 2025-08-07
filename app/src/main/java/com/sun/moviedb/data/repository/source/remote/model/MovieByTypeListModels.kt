package com.sun.moviedb.data.repository.source.remote.model

data class SeoOnPage(
    val og_type: String?,
    val titleHead: String?,
    val descriptionHead: String?,
    val og_image: List<String>?,
    val og_url: String?
)

data class BreadCrumb(
    val name: String?,
    val slug: String? = null,
    val isCurrent: Boolean?,
    val position: Int?
)

data class Params(
    val type_slug: String?,
    val slug: String? = null,
    val keyword: String? = null,
    val filterCategory: List<String>?,
    val filterCountry: List<String>?,
    val filterYear: List<String>?,
    val filterType: List<String>?,
    val sortField: String?,
    val sortType: String?,
    val pagination: Pagination?
)

data class MovieByTypeListData(
    val seoOnPage: SeoOnPage?,
    val breadCrumb: List<BreadCrumb>?,
    val titlePage: String?,
    val items: List<MovieItem>?,
    val params: Params?,
    val type_list: String?,
    val APP_DOMAIN_FRONTEND: String?,
    val APP_DOMAIN_CDN_IMAGE: String?
)

data class MovieByTypeListResponse(
    val status: Any?,
    val msg: String?,
    val data: MovieByTypeListData?
)

