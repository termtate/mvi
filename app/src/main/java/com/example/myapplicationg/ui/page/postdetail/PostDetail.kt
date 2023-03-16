package com.example.myapplicationg.ui.page.postdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplicationg.ui.widgets.pagingItems
import com.example.myapplicationg.ui.widgets.Comment
import com.example.myapplicationg.ui.widgets.CommentParameterProvider
import com.example.myapplicationg.ui.widgets.ItemPlaceHolder
import com.example.myapplicationg.ui.widgets.PostData
import com.example.myapplicationg.ui.widgets.PostHeader
import com.example.myapplicationg.ui.widgets.PostDataParameterProvider
import com.example.myapplicationg.ui.widgets.horizontalPadding
import kotlinx.coroutines.flow.flowOf


//@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PostDetail(
    @PreviewParameter(PostDetailParameterProvider::class) state: PostDetailState,
    sentSnackBar: () -> Unit = {},
    onBack: () -> Unit = {},
    onLikeChanged: (Int) -> Unit = {},
    onSendComment: (String) -> Unit = {},
    onNoNetWork: (LoadState.Error) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }

    if (state.snackBarMsg != null) {
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar(
                state.snackBarMsg
            )
            sentSnackBar()
        }
    }

    if (state.postData == null) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val comments = state.comments.collectAsLazyPagingItems()
        Scaffold(
            topBar = {
                TopCommentBar(onBack = onBack)
            },
            bottomBar = {
                BottomCommentBar(onSend = {
                    onSendComment(it)
                    comments.refresh()
                })
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) {paddingValues ->
            LazyColumn(Modifier.padding(paddingValues)) {
                item {
                    PostContent(state.postData)
                }

                item {
                    Spacer(Modifier.height(3.dp))
                }
                stickyHeader {
                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontalPadding, 0.dp)
                        ) {
                            Text(
                                text = "评论",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                pagingItems(
                    items = comments,
                    onError = onNoNetWork
                ) { comment ->
                    if (comment != null) {
                        Comment(
                            comment,
                            onLikeChanged = { onLikeChanged(comment.id) }
                        )
                        Divider()
                    } else {
                        ItemPlaceHolder()
                    }
                }
            }
        }
    }
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopCommentBar(
    onBack: () -> Unit = {}
) {
    TopAppBar(
        title = { Text("详情") },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
    )
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomCommentBar(
    onSend: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    BottomAppBar(
        tonalElevation = 2.dp
    ) {
        TextField(
            modifier = Modifier
                .padding(5.dp, 2.dp)
                .weight(5f),
            value = text,
            onValueChange = {text = it},
            placeholder = { Text("发评论") },
            shape = RoundedCornerShape(40),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                placeholderColor = MaterialTheme.colorScheme.secondary
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = { onSend(text) }) {
                Text(
                    text = "发布",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}


@Composable
private fun PostContent(
    @PreviewParameter(PostDataParameterProvider::class) postData: PostData
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontalPadding, 0.dp)
                .padding(bottom = 5.dp)) {
            Column {
                PostHeader(postData.userName, postData.userIcon, postData.publishDate)
                Text(
                    text = postData.content,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(0.dp, 5.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


class PostDetailParameterProvider : PreviewParameterProvider<PostDetailState> {
    override val values: Sequence<PostDetailState>
        get() = listOf(
            PostDetailState(

                PostDataParameterProvider().values.first(),
                flowOf(PagingData.from(CommentParameterProvider().values.toList())),
                "123"
            )
        ).asSequence()
}


