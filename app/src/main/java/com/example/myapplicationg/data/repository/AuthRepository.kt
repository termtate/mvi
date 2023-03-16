package com.example.myapplicationg.data.repository

import android.content.ContentValues
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    loginRepository: LoginRepository
) {
    val authExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i(ContentValues.TAG, ": ${throwable.cause}")
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401, 403 -> loginRepository.setLoginState(false)

                }
            }
        }
    }
}