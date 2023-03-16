package com.example.myapplicationg.data.network

import com.example.myapplicationg.data.network.bean.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface LoginDataSource {
    @FormUrlEncoded
    @POST("$API_PREFIX/login/access-token")
    suspend fun loginAccessToken(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Token


}