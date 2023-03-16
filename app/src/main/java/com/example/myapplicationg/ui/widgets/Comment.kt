package com.example.myapplicationg.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUpAlt
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.myapplicationg.di.NetworkModule.BASE_URL
import com.example.myapplicationg.ui.utils.BasePublishItem
import java.util.*

@Preview(showBackground = true)
@Composable
fun Comment(
    @PreviewParameter(CommentParameterProvider::class) comment: CommentData,
    onLikeChanged: () -> Unit = {},
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(
                Modifier
                    .padding(horizontalPadding, 5.dp)
                    .fillMaxWidth()
            ) {
                SubcomposeAsyncImage(
                    model = BASE_URL + "api/v1/" + comment.userIcon,
                    loading = { CircularProgressIndicator() },
                    contentDescription = null,
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .size(35.dp)
                )
                Column(
                    Modifier.padding(start = 8.dp),
                ) {
                    Row {
                        Column {
                            Text(
                                text = comment.userName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = comment.publishDate,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            ) {
                                IconWithLabel(
                                    icon = if (comment.isLiked) Icons.Filled.ThumbUpAlt
                                    else Icons.Outlined.ThumbUpAlt,
                                    text = comment.likes.toString(),
                                    onClick = onLikeChanged
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = comment.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

            }
        }
    }

}

data class CommentData(
    override val id: Int,
    override val content: String,
    override val userName: String,
    override val userIcon: String,
    override val publishDate: String,
    override val likes: Int,
    override val isLiked: Boolean,
) : BasePublishItem


class CommentParameterProvider : PreviewParameterProvider<CommentData> {
    override val values: Sequence<CommentData>
        get() = listOf(
            CommentData(
                1,
                "因此，若要构建混合应用，我们建议使用基于 fragment 的 Navigation 组件，并使用 fragment 存储基于 view 的屏幕、Compose 屏幕和同时使用 view 和 Compose 的屏幕。应用中的每个屏幕 fragment 都是一个可组合项的封装容器，下一步是将所有这些屏幕与 Navigation Compose 结合在一起，并移除所有 fragment。",
                "termtate",
                "https://img.51miz.com/Element/00/88/82/42/c21e019b_E888242_0f2360ce.png",
                "11:20",
                13,
                false
            )
        ).asSequence()
}
