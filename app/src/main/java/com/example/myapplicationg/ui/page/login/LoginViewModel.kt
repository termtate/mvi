package com.example.myapplicationg.ui.page.login

import android.net.Uri
import com.example.myapplicationg.data.repository.LoginRepository
import com.example.myapplicationg.data.repository.UserInfoRepository
import com.example.myapplicationg.ui.utils.BaseViewModel
import com.example.myapplicationg.ui.utils.UiIntent
import com.example.myapplicationg.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    ioContext: CoroutineContext,
    private val loginRepository: LoginRepository
) : BaseViewModel<LoginState, LoginIntent>(ioContext) {
    override fun createInitialState() = LoginState(isError = false, onLoggedIn = false)

    override suspend fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login(intent.name, intent.password)
            is LoginIntent.Register -> register(intent.registerData)
        }
    }

    private suspend fun login(name: String, password: String) {
        try {
            userInfoRepository.login(name, password)
            setState { copy(onLoggedIn = true) }
            loginRepository.setLoginState(true)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> {
                    setState { copy(isError = true) }
                }
                else -> throw e
            }
        }
    }

    private suspend fun register(registerData: RegisterData) {
        try {
            userInfoRepository.register(
                registerData.name,
                registerData.icon,
                registerData.phoneNumber,
                registerData.password
            )
            userInfoRepository.login(registerData.name, registerData.password)
            setState { copy(onLoggedIn = true) }
            loginRepository.setLoginState(true)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    setState { copy(isError = true) }
                }
                else -> throw e
            }
        }
    }
}


data class LoginState(
    val isError: Boolean,
    val onLoggedIn: Boolean
) : UiState

sealed class LoginIntent : UiIntent {
    data class Login(val name: String, val password: String) : LoginIntent()
    data class Register(val registerData: RegisterData) : LoginIntent()
}


data class RegisterData(
    val name: String,
    val password: String,
    val phoneNumber: String,
    val icon: Uri
)
