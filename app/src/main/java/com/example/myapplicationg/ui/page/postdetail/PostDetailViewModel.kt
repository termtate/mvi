package com.example.myapplicationg.ui.page.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplicationg.data.repository.PublishRepository
import com.example.myapplicationg.ui.widgets.CommentData
import com.example.myapplicationg.ui.widgets.PostData
import com.example.myapplicationg.ui.utils.BaseViewModel
import com.example.myapplicationg.ui.utils.UiIntent
import com.example.myapplicationg.ui.utils.UiState
import com.example.myapplicationg.usecase.PublishItemConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class PostDetailViewModel @Inject constructor(
    ioContext: CoroutineContext,
    savedStateHandle: SavedStateHandle,
    private val publishRepository: PublishRepository,
    private val publishItemConverter: PublishItemConverter
) : BaseViewModel<PostDetailState, PostDetailIntent>(ioContext) {
    companion object {
        const val NO_NET = "当前无网络，为你展示最近消息"
        const val SEND_COMMENT = "已发送"
    }


    private val postId: Int = checkNotNull(savedStateHandle["postId"])

    init {
        viewModelScope.launch(ioContext) {
            initPost(postId)
            initComments(postId)
        }
    }

    override fun createInitialState(): PostDetailState = PostDetailState(
        null,
        emptyFlow(),
        null
    )

    override suspend fun handleIntent(intent: PostDetailIntent) {
        when (intent) {
            is PostDetailIntent.OnLikeChange -> { updateLike(intent.commentId) }
            is PostDetailIntent.PublishComment -> { publishComment(intent.content) }
            is PostDetailIntent.SnackBarShown -> setState { copy(snackBarMsg = null) }
            is PostDetailIntent.ShowNoNetworkSnackBar -> setState { copy(snackBarMsg = NO_NET) }
        }
    }

    private suspend fun updateLike(commentId: Int) {
        publishRepository.changeItemLike(commentId)
    }

    private suspend fun publishComment(content: String) {
        publishRepository.publishComment(content, postId)
        setState { copy(snackBarMsg = SEND_COMMENT) }
    }

    private suspend fun initPost(postId: Int) {
        val post = publishRepository.readPostFromDB(postId)
        setState { copy(postData = publishItemConverter.toPostData(post)) }
    }

    private fun initComments(postId: Int) {
        setState { copy(
            comments = publishRepository
                .getCommentsPager(postId)
                .map { pagingData ->
                    pagingData.map {
                        publishItemConverter.toCommentsData(it)
                    }
                }
                .cachedIn(viewModelScope)
        ) }
    }
}

data class PostDetailState(
    val postData: PostData?,
    val comments: Flow<PagingData<CommentData>>,
    val snackBarMsg: String?
) : UiState

sealed class PostDetailIntent : UiIntent {
    data class OnLikeChange(val commentId: Int) : PostDetailIntent()
    data class PublishComment(val content: String) : PostDetailIntent()
    object SnackBarShown : PostDetailIntent()
    object ShowNoNetworkSnackBar : PostDetailIntent()

}
