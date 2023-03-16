package com.example.myapplicationg.data.repository

import com.example.myapplicationg.data.AccountDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val accountDataStore: AccountDataStore,
) {
    private val _loginFlow = MutableStateFlow(true)
    val loginFlow = _loginFlow.asStateFlow()

    suspend fun getHeader() = "bearer ${accountDataStore.tokenFlow.first()}"

    fun setLoginState(state: Boolean) {
        _loginFlow.update { state }
    }
}