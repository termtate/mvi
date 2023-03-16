package com.example.myapplicationg.data.network.interceptor

import android.content.ContentValues.TAG
import android.util.Log
import com.example.myapplicationg.data.repository.LoginRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.Route
import retrofit2.HttpException
import javax.inject.Inject


class AuthenticationInterceptor @Inject constructor(
    private val loginRepository: LoginRepository
) : Interceptor, Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        refreshToken()
        return null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val header = runBlocking { loginRepository.getHeader() }
        Log.i(TAG, "intercept: $header")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", header)
            .build()
        return chain.proceed(request)
    }

    private fun refreshToken() {
        loginRepository.setLoginState(false)
    }
}