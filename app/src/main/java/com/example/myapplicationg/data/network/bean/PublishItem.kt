package com.example.myapplicationg.data.network.bean

import com.example.myapplicationg.data.db.entity.PublishItem
import java.time.LocalDateTime


enum class ItemType {
    posts, comments
}

data class PublishItem(
    val content: String,
    val id: Int,
    val is_liked: Boolean,
    val type: String,
    val owner: UserInfo,
    val publish_date: LocalDateTime,
    val likes_num: Int,
    val comments_num: Int,
    val belongs: Int
) {
    fun toDbItem() = PublishItem(
        itemId = id,
        content = content,
        publishTime = publish_date,
        type = type,
        userOwnerId = owner.id,
        isLiked = is_liked,
        userIcon = owner.icon,
        userName = owner.name,
        likes = likes_num,
        comments = comments_num,
        belongs = belongs
    )
}
