package com.example.myapplicationg.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "publish_items")
data class PublishItem(
    @PrimaryKey val itemId: Int,
    val content: String,
    val publishTime: LocalDateTime,
    val type: String,
    val userOwnerId: Int,
    val isLiked: Boolean,
    val likes: Int,
    val comments: Int,
    val belongs: Int,

    val userIcon: String,
    val userName: String
)
