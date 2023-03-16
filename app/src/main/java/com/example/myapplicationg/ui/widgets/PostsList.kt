package com.example.myapplicationg.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState

import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostsList(
    posts: LazyPagingItems<PostData>,
    listState: LazyListState,
    onLikeChanged: (postId: Int) -> Unit,
    openComment: (postId: Int) -> Unit,
    noNetworkError: (LoadState.Error) -> Unit
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(400)
        posts.refresh()

        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

    Box(
        Modifier
            .pullRefresh(state)
            .fillMaxSize()) {
        LazyColumn(
            state = listState
        ) {
            if (!refreshing) {
                pagingItems(
                    items = posts,
                    onError = noNetworkError
                ) {post ->
                    if (post != null) {
                        Post(
                            post,
                            onLikeChanged = { onLikeChanged(post.id) },
                            openComment = { openComment(post.id) }
                        )
                        Divider()
                    } else {
                        ItemPlaceHolder()
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier.align( Alignment.TopCenter)
        )
    }
}