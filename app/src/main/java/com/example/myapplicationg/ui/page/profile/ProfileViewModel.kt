package com.example.myapplicationg.ui.page.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myapplicationg.data.network.bean.UserInfo
import com.example.myapplicationg.ui.widgets.CommentData
import com.example.myapplicationg.ui.widgets.PostData
import com.example.myapplicationg.ui.utils.BaseViewModel
import com.example.myapplicationg.ui.utils.UiIntent
import com.example.myapplicationg.ui.utils.UiState
import com.example.myapplicationg.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class ProfileViewModel @Inject constructor(
    ioContext: CoroutineContext,
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(ioContext) {
            val profile = profileUseCase.getProfile(this)
            _uiState.update { profile }
        }
    }
}


data class ProfileState(
    val userInfo: UserInfo = UserInfo(),
    val posts: Flow<PagingData<PostData>> = emptyFlow(),
    val comments: Flow<PagingData<CommentData>> = emptyFlow()
) : UiState
