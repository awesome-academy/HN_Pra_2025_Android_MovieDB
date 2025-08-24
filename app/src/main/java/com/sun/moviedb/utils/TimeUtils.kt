package com.sun.moviedb.utils

object TimeUtils {
    fun getTimeAgo(time: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "Vừa xong"
            minutes == 1L -> "1 phút trước"
            minutes < 60 -> "$minutes phút trước"
            hours == 1L -> "1 giờ trước"
            hours < 24 -> "$hours giờ trước"
            days == 1L -> "Hôm qua"
            days < 7 -> "$days ngày trước"
            days < 30 -> "${days / 7} tuần trước"
            days < 365 -> "${days / 30} tháng trước"
            else -> "${days / 365} năm trước"
        }
    }

}