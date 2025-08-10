package com.sun.moviedb.data.model

/*
* used in APIs: phim-moi, theloai
*               is a attribute of "Data" Model
* */
data class Item(
    val _id: String = "",
    val category: List<Category> = emptyList(),
    val chieurap: Boolean = false,
    val country: List<Country> = emptyList(),
    val episode_current: String = "",
    val lang: String = "",
    val name: String = "",
    val origin_name: String = "",
    val poster_url: String = "",
    val quality: String = "",
    val slug: String = "",
    val sub_docquyen: Boolean = false,
    val thumb_url: String = "",
    val time: String = "",
    val type: String = "",
    val year: Int = 0
)
