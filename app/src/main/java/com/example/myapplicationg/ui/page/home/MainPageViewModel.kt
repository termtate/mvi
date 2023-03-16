package com.example.myapplicationg.ui.page.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplicationg.data.repository.PublishRepository
import com.example.myapplicationg.ui.widgets.PostData
import com.example.myapplicationg.ui.page.profile.ProfileState
import com.example.myapplicationg.ui.utils.BaseViewModel
import com.example.myapplicationg.ui.utils.UiIntent
import com.example.myapplicationg.ui.utils.UiState
import com.example.myapplicationg.usecase.ProfileUseCase
import com.example.myapplicationg.usecase.PublishItemConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val publishRepository: PublishRepository,
    private val ioContext: CoroutineContext,
    private val publishItemConverter: PublishItemConverter,
    private val profileUseCase: ProfileUseCase
) : BaseViewModel<MainPageUiState, MainPageIntent>(ioContext) {
    override fun createInitialState() = MainPageUiState()

    init {
        initPosts()
        initProfile()
    }

    private fun initPosts() {
        viewModelScope.launch(ioContext) {
            setState { copy(
                pager = publishRepository
                    .postsPager
                    .map { pagingData ->
                        pagingData.map {
                            publishItemConverter.toPostData(it)
                        }
                    }
                    .cachedIn(viewModelScope),
            ) }
        }
    }

    private fun initProfile() {
        viewModelScope.launch(ioContext) {
            val profile = profileUseCase.getProfile(viewModelScope)
            setState { copy(profileState = profile) }
        }
    }

    override suspend fun handleIntent(intent: MainPageIntent) {
        when (intent) {
            is MainPageIntent.OnLikeChange -> { updateLike(intent.id) }
            is MainPageIntent.PublishPost -> { publishPost(intent.content) }
            is MainPageIntent.SnackBarShown -> setState { copy(noNet = false, postSent = false) }
            is MainPageIntent.ShowSnackBar -> setState { copy(noNet = true) }
        }
    }

    private suspend fun updateLike(postId: Int) {
        try {
            publishRepository.changeItemLike(postId)
        } catch (e: HttpException) {
            setState { copy(noNet = true) }
        }
    }

    private suspend fun publishPost(content: String) {
        publishRepository.publishPost(content)
        setState { copy(postSent = true) }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()

    }

}


data class MainPageUiState(
    val profileState: ProfileState = ProfileState(),
    val pager: Flow<PagingData<PostData>> = emptyFlow(),
    val noNet: Boolean = false,
    val postSent: Boolean = false
) : UiState

sealed class MainPageIntent : UiIntent {
    data class OnLikeChange(val id: Int) : MainPageIntent()
    data class PublishPost(val content: String) : MainPageIntent()
    object SnackBarShown : MainPageIntent()
    object ShowSnackBar : MainPageIntent()
}

