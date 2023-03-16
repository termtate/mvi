package com.example.myapplicationg.di

import com.example.myapplicationg.data.network.LoginDataSource
import com.example.myapplicationg.data.network.PublishDataSource
import com.example.myapplicationg.data.network.UserDataSource
import com.example.myapplicationg.data.network.interceptor.AuthenticationInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Singleton


class LocalDateAdapter : JsonDeserializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime = LocalDateTime.parse(json?.asString)
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "http://10.0.2.2:8000/"

    @Provides
    @Singleton
    fun provideClient(
        interceptor: AuthenticationInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient
    ): Retrofit = Retrofit
        .Builder()
        .client(httpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateAdapter())
                    .create()
            )
        ).build()

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit
    ): UserDataSource = retrofit.create(UserDataSource::class.java)

    @Provides
    @Singleton
    fun providePublishService(
        retrofit: Retrofit
    ): PublishDataSource = retrofit.create(PublishDataSource::class.java)

    @Provides
    @Singleton
    fun provideLoginService(
        retrofit: Retrofit
    ): LoginDataSource = retrofit.create(LoginDataSource::class.java)
}