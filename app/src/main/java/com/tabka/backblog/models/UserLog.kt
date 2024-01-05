package com.tabka.backblog.models

data class UserLog(
    val id: String,
    val name: String,
    val is_visible: Boolean,
    val movie_ids: List<Int>,
    val watched_ids: List<Int>,
    val priority: Int,
)