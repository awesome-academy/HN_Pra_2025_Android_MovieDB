package com.sun.moviedb.data.repository.source.remote.model

data class SeoOnPage(
    val og_type: String?,
    val titleHead: String?,
    val descriptionHead: String?,
    val og_image: List<String>?,
    val og_url: String?
)

data class BreadCrumb(
    val name: String? = null,
    val slug: String? = null,
    val isCurrent: Boolean? = null,
    val position: Int? = null
)

data class Params(
    val type_slug: String? = null,
    val slug: String? = null,
    val keyword: String? = null,
    val filterCategory: List<String>? = null,
    val filterCountry: List<String>? = null,
    val filterYear: List<String>? = null,
    val filterType: List<String>? = null,
    val sortField: String? = null,
    val sortType: String? = null,
    val pagination: Pagination? = null
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
