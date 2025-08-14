package com.sun.moviedb.utils

object LanguageMapper {
    private val codeToDisplayName: Map<String, String> = mapOf(
        "vietsub" to "Vietsub",
        "thuyet-minh" to "Thuyết Minh",
        "long-tieng" to "Lồng Tiếng"
    )

    fun getDisplayName(code: String): String {
        return codeToDisplayName[code] ?: code
    }

    fun getLanguageCodes(): List<String> = codeToDisplayName.keys.toList()
}
