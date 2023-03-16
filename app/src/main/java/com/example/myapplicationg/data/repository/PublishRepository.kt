package com.example.myapplicationg.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.myapplicationg.data.db.DbDataSource
import com.example.myapplicationg.data.network.PublishDataSource
import com.example.myapplicationg.data.network.interceptor.AuthenticationInterceptor
import com.example.myapplicationg.data.paging.ItemsRemoteMediator
import com.example.myapplicationg.data.paging.ProfilePagingSource
import com.example.myapplicationg.di.LocalDateAdapter
import com.example.myapplicationg.di.NetworkModule
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PublishRepository @Inject constructor(
    private val publishDataSource: PublishDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    private val dbDataSource: DbDataSource,
    private val loginRepository: LoginRepository
) {
    companion object {
        const val PAGE_SIZE = 10
        const val PREFETCH_DISTANCE = 2
    }

    suspend fun readPosts(page: Int, size: Int) = withContext(ioDispatcher) {
        publishDataSource.readPostsMe(page, size)
    }

    suspend fun readComments(postId: Int, page: Int, size: Int) = withContext(ioDispatcher) {
        publishDataSource.readPostCommentsMe(postId, page, size)
    }

    @OptIn(ExperimentalPagingApi::class)
    val postsPager = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE),
        remoteMediator = ItemsRemoteMediator(ItemType.Post, dbDataSource, this, loginRepository)
    ) {
        dbDataSource.publishItemDao().postsPagingSource()
    }.flow

    @OptIn(ExperimentalPagingApi::class)
    fun getCommentsPager(postId: Int) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE),
        remoteMediator = ItemsRemoteMediator(ItemType.Comment(postId), dbDataSource, this, loginRepository)
    ) {
        dbDataSource.publishItemDao().commentsPagingSource(postId)
    }.flow

    fun getProfilePager(type: String, userId: Int) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE),
    ) {
        ProfilePagingSource(type, userId, this)
    }.flow

    suspend fun getUserItems(userId: Int, type: String, page: Int) = withContext(ioDispatcher) {
        publishDataSource.readUserItems(type, userId, page, PAGE_SIZE)
    }

    private suspend fun getItem(itemId: Int) = withContext(ioDispatcher) {
        dbDataSource.publishItemDao().getItem(itemId)
    }

    suspend fun changeItemLike(itemId: Int) = withContext(ioDispatcher) {
        val item = getItem(itemId)
        var isLiked = item.isLiked
        var likes = item.likes
        if (isLiked) {
            isLiked = false
            likes--
            publishDataSource.unlikeItem(itemId)
        } else {
            isLiked = true
            likes++
            publishDataSource.likeItem(itemId)
        }
        dbDataSource.publishItemDao().updateItems(item.copy(likes = likes, isLiked = isLiked))
    }

    suspend fun readPostFromDB(postId: Int) = withContext(ioDispatcher) {
        dbDataSource.publishItemDao().getItem(postId)
    }

    suspend fun publishPost(content: String) = withContext(ioDispatcher) {
        publishDataSource.publishItem("post", ItemSend(content))

    }

    suspend fun publishComment(content: String, postId: Int) = withContext(ioDispatcher) {
        publishDataSource.publishItem("comment", ItemSend(content, postId))
    }

}

sealed class ItemType(val name: String, val belongs: Int) {
    object Post : ItemType("post", -1)
    data class Comment(val postId: Int) : ItemType("comment", postId)
}

data class ItemSend(
    val content: String,
    val belongs: Int = -1
)




