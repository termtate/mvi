package com.example.myapplicationg.data.network.bean

data class Page(
    val items: List<PublishItem>,
    val total: Int,
    val page: Int,
    val size: Int,
)