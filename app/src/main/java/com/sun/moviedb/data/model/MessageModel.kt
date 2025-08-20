package com.sun.moviedb.data.model

data class MessageModel(
    val id: String = "",
    val senderId: String = "",
    val senderName: String= "",
    val content: String = "",
    val linkAvt: String = "",
    val createAt: Long = System.currentTimeMillis()
)
