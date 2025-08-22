package com.sun.moviedb.data.model

data class Item(
    val id: String = "",
    val category: List<Category> = emptyList(),
    val chieurap: Boolean = false,
    val country: List<Country> = emptyList(),
    val episodeCurrent: String = "",
    val lang: String = "",
    val name: String = "",
    val originName: String = "",
    val posterUrl: String = "",
    val quality: String = "",
    val slug: String = "",
    val thumbUrl: String = "",
    val time: String = "",
    val type: String = "",
    val year: Int = 0
)

