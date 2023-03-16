package com.example.myapplicationg.ui.page

import androidx.lifecycle.ViewModel
import com.example.myapplicationg.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    loginRepository: LoginRepository
) : ViewModel() {

    val loginFlow = loginRepository.loginFlow
}