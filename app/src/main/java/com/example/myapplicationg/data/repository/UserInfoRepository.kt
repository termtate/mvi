package com.example.myapplicationg.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.myapplicationg.data.AccountDataStore
import com.example.myapplicationg.data.network.LoginDataSource
import com.example.myapplicationg.data.network.UserDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val accountDataStore: AccountDataStore,
    private val loginDataSource: LoginDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
) {
    suspend fun login(userName: String, password: String): String = withContext(ioDispatcher) {
        loginDataSource.loginAccessToken(userName, password).access_token.also {
            accountDataStore.setToken(it)
        }
    }

    suspend fun getUserInfo(userId: Int) = withContext(ioDispatcher) {
        userDataSource.readUser(userId)
    }

    suspend fun getSelfInfo() = withContext(ioDispatcher) {
        userDataSource.readUserMe()
    }

    suspend fun register(
        userName: String,
        icon: Uri,
        phoneNumber: String,
        password: String
    ) = withContext(ioDispatcher) {
        val bitmap = uriToBitmap(icon)
        val imgContent = bitmapToByteArray(bitmap)

        val mediaType = "multipart/form-data".toMediaType()

        val body = MultipartBody.Part.createFormData(
            "icon",
            "1.jpg",
            imgContent.toRequestBody(mediaType)
        )

        userDataSource.createUser(
            userName.toRequestBody(mediaType),
            body,
            phoneNumber.toRequestBody(mediaType),
            password.toRequestBody(mediaType)
        )
    }

    private suspend fun uriToBitmap(uri: Uri) = withContext(ioDispatcher) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable

        return@withContext (result as BitmapDrawable).bitmap
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

}
