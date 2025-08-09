package com.sun.moviedb.data.model

/*
* used in APIs: phim-moi, theloai
*               is one attribute of "Data" Model
* */
data class Item(
    val _id: String,
    val category: List<Category>,
    val chieurap: Boolean,
    val country: List<Country>,
    val episode_current: String,
    val lang: String,
    val name: String,
    val origin_name: String,
    val poster_url: String,
    val quality: String,
    val slug: String,
    val sub_docquyen: Boolean,
    val thumb_url: String,
    val time: String,
    val type: String,
    val year: Int
)