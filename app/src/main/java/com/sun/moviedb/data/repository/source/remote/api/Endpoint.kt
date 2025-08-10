package com.sun.moviedb.data.repository.source.remote.api

import com.sun.moviedb.utils.Constants

object Endpoint {
    const val GET_NEWEST_MOVIE = "${Constants.BASE_URL}/danh-sach/phim-moi-cap-nhat-v3/"
    const val GET_MOVIE_DETAIL = "${Constants.BASE_URL}/phim/" // + {slug}
    const val GET_SERIES_MOVIE = "${Constants.BASE_URL}/v1/api/danh-sach/" // + {type-list}
    const val GET_FILTER_MOVIE = "${Constants.BASE_URL}/v1/api/the-loai/" // + {type_list}
    const val SEARCH_MOVIE = "${Constants.BASE_URL}/v1/api/tim-kiem"
    const val GET_CATEGORIES = "${Constants.BASE_URL}/the-loai"
    const val GET_COUNTRIES = "${Constants.BASE_URL}/quoc-gia"
}

