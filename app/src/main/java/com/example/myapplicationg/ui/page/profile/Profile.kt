package com.example.myapplicationg.ui.page.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.myapplicationg.data.network.bean.UserInfo
import com.example.myapplicationg.di.NetworkModule
import com.example.myapplicationg.ui.widgets.pagingItems
import com.example.myapplicationg.ui.widgets.Comment
import com.example.myapplicationg.ui.widgets.CommentParameterProvider
import com.example.myapplicationg.ui.widgets.Post
import com.example.myapplicationg.ui.widgets.PostDataParameterProvider
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true, showBackground = false, device = "spec:width=411dp,height=891dp"
)
@Composable
fun Profile(
    @PreviewParameter(ProfileParameterProvider::class) state: ProfileState,
    onNoNetwork: (LoadState.Error) -> Unit = {}
) {
    var tabState by remember { mutableStateOf(0) }
    val titles = listOf("动态", "评论")

    val posts = state.posts.collectAsLazyPagingItems()
    val comments = state.comments.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    "个人主页",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )},
                actions = { Icon(Icons.Filled.MoreVert, contentDescription = null) }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column {
                ListItem(
                    headlineText = {
                        Text(text = state.userInfo.name)
                    },
                    supportingText = {
                        Text(text = "ID: ${state.userInfo.id}")
                    },
                    leadingContent = {
                        SubcomposeAsyncImage(
                            model = NetworkModule.BASE_URL + "api/v1/" + state.userInfo.icon,
                            loading = { CircularProgressIndicator() },
                            contentDescription = null,
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .size(70.dp),
                        )
                    }
                )
                TabRow(selectedTabIndex = tabState) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = tabState == index,
                            onClick = { tabState = index },
                            text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                        )
                    }
                }
                LazyColumn {
                    when (tabState) {
                        0 -> pagingItems(
                            items = posts,
                            onError = onNoNetwork
                        ) { post ->
                            if (post != null) {
                                Post(postData = post)
                                Divider()
                            } else {
                                Spacer(modifier = Modifier.height(70.dp))
                            }
                        }
                        1 -> pagingItems(
                            items = comments,
                            onError = onNoNetwork
                        ) { comment ->
                            if (comment != null) {
                                Comment(comment = comment)
                                Divider()
                            } else {
                                Spacer(modifier = Modifier.height(70.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


class ProfileParameterProvider : PreviewParameterProvider<ProfileState> {
    override val values: Sequence<ProfileState>
        get() = listOf(
            ProfileState(
                UserInfo(
                    1,
                    "平凡的小伟射手",
                    "https://img.51miz.com/Element/00/88/82/42/c21e019b_E888242_0f2360ce.png"
                ),
                flowOf(PagingData.from(PostDataParameterProvider().values.toList())),
                flowOf(PagingData.from(CommentParameterProvider().values.toList()))
            )

        ).asSequence()
}