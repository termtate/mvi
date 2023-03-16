package com.example.myapplicationg.usecase

import com.example.myapplicationg.data.db.entity.PublishItem
import com.example.myapplicationg.ui.widgets.CommentData
import com.example.myapplicationg.ui.widgets.PostData
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PublishItemConverter @Inject constructor(
    private val formatDateUseCase: FormatDateUseCase
) {
    fun toPostData(publishItem: PublishItem) = PostData(
        id = publishItem.itemId,
        content = publishItem.content,
        userName = publishItem.userName,
        userIcon = publishItem.userIcon,
        publishDate = formatDateUseCase(publishItem.publishTime),
        likes = publishItem.likes,
        isLiked = publishItem.isLiked,
        comments = publishItem.comments
    )

    fun toCommentsData(publishItem: PublishItem) = CommentData(
        id = publishItem.itemId,
        content = publishItem.content,
        userName = publishItem.userName,
        userIcon = publishItem.userIcon,
        publishDate = formatDateUseCase(publishItem.publishTime),
        likes = publishItem.likes,
        isLiked = publishItem.isLiked,
    )

}