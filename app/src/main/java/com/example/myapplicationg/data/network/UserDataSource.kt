package com.example.myapplicationg.data.network

import com.example.myapplicationg.data.network.bean.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

const val API_PREFIX = "/api/v1"

const val USERS = "users"

interface UserDataSource {
    @Multipart
    @POST("$API_PREFIX/$USERS")
    suspend fun createUser(
        @Part("user_name") userName: RequestBody,
        @Part icon: MultipartBody.Part,
        @Part("user_phone_number") phoneNumber: RequestBody,
        @Part("user_password") password: RequestBody
    )

    @GET("$API_PREFIX/$USERS/{user_id}")
    suspend fun readUser(
        @Path("user_id") userId: Int
    ): UserInfo

    @GET("$API_PREFIX/$USERS/me")
    suspend fun readUserMe(): UserInfo
}