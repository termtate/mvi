package com.example.myapplicationg.ui.widgets

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUpAlt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.myapplicationg.di.NetworkModule.BASE_URL
import com.example.myapplicationg.ui.utils.BasePublishItem
import java.util.*


@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun Post(
    @PreviewParameter(PostDataParameterProvider::class) postData: PostData,
    onLikeChanged: () -> Unit = {},
    openComment: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.clickable { isExpanded = !isExpanded },
    ) {
        Column {
            PostHeader(postData.userName, postData.userIcon, postData.publishDate)
            Text(
                text = postData.content,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontalPadding, 5.dp)
                    .animateContentSize()
            )
            PostFooter(
                onLikeChanged = onLikeChanged,
                likes = postData.likes,
                comments = postData.comments,
                isLiked = postData.isLiked,
                openComment = openComment
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostHeader(userName: String, userIcon: String, publishDate: String) {
    Row {
        ListItem(
            headlineText = {
                Text(userName)
            },
            supportingText = {
                Text(publishDate)
            },
            leadingContent = {
                SubcomposeAsyncImage(
                    model = BASE_URL + "api/v1/" + userIcon,
                    loading = { CircularProgressIndicator() },
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                )
            }
        )
    }
}


@Composable
fun PostFooter(
    likes: Int,
    comments: Int,
    isLiked: Boolean,
    onLikeChanged: () -> Unit,
    openComment: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(90.dp, 0.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconWithLabel(
            icon = Icons.Outlined.ChatBubbleOutline,
            text = comments.toString(),
            onClick = openComment
        )
        IconWithLabel(
            icon = if (isLiked) Icons.Filled.ThumbUpAlt else Icons.Outlined.ThumbUpAlt,
            text = likes.toString(),
            onClick = onLikeChanged
        )

    }
}

data class PostData(
    override val id: Int,
    override val content: String,
    override val userName: String,
    override val userIcon: String,
    override val publishDate: String,
    override val likes: Int,
    override val isLiked: Boolean,
    val comments: Int
) : BasePublishItem


class PostDataParameterProvider : PreviewParameterProvider<PostData> {
    override val values: Sequence<PostData>
        get() = listOf(
            PostData(
                1,
                "Compose 使用声明性 API，这意味着您只需描述界面，Compose 会负责完成其余工作。这类 API 十分直观 - 易于探索和使用：“我们的主题层更加直观，也更加清晰。我们能够在单个 Kotlin 文件中完成之前需要在多个 XML 文件中完成的任务，这些 XML 文件负责通过多个分层主题叠加层定义和分配属性。”(Twitter)",
                "termtate",
                "https://img.51miz.com/Element/00/88/82/42/c21e019b_E888242_0f2360ce.png",
                "8:11",
                54,
                true,
                2
            )

    ).asSequence()
}