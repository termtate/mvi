package com.example.myapplicationg.data.network

import com.example.myapplicationg.data.network.bean.Page
import com.example.myapplicationg.data.repository.ItemSend
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val ITEMS = "items"

interface PublishDataSource {
    @GET("$API_PREFIX/$ITEMS/posts")
    suspend fun readPosts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Page

    @GET("$API_PREFIX/$ITEMS/posts/me")
    suspend fun readPostsMe(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Page


    @GET("$API_PREFIX/$ITEMS/comments/me")
    suspend fun readPostCommentsMe(
        @Query("post_id") postId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Page

    @GET("$API_PREFIX/$ITEMS/{item_type}/{user_id}")
    suspend fun readUserItems(
        @Path("item_type") itemType: String,
        @Path("user_id") userId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Page

    @POST("$API_PREFIX/$ITEMS/{item_type}")
    suspend fun publishItem(
        @Path("item_type") itemType: String,
        @Body body: ItemSend
    )

    @FormUrlEncoded
    @POST("$API_PREFIX/$ITEMS/like")
    suspend fun likeItem(
        @Field("item_id") itemId: Int
    )

    @DELETE("$API_PREFIX/$ITEMS/unlike/{item_id}")
    suspend fun unlikeItem(
        @Path("item_id") itemId: Int
    )
}