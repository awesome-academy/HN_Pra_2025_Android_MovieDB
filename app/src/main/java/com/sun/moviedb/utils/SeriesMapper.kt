package com.sun.moviedb.utils

object SeriesMapper {
    private val codeToDisplayName: Map<String, String> = mapOf(
        "phim-bo" to "Phim Bộ",
        "phim-le" to "Phim Lẻ",
        "hoat-hinh" to "Hoạt Hình",
        "phim-vietsub" to "Phim Vietsub",
        "phim-thuyet-minh" to "Phim Thuyết Minh",
        "phim-long-tieng" to "Phim Lồng Tiếng"
    )

    fun getDisplayName(code: String): String {
        return codeToDisplayName[code] ?: code
    }

    fun getSeriesCodes(): List<String> = codeToDisplayName.keys.toList()
}
