package com.example.myapplicationg.ui.utils

interface BasePublishItem {
    val id: Int
    val content: String
    val userName: String
    val userIcon: String
    val publishDate: String
    val likes: Int
    val isLiked: Boolean
}
