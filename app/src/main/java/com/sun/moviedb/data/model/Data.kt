package com.sun.moviedb.data.model

/* *
* used in APIs: quoc-gia, tim-kiem
* */
data class Data(
    val items: List<Item> = emptyList(),
    val params: Params = Params(),
    val titlePage: String = "",
    val type_list: String = ""
)
