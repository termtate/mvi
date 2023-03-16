package com.example.myapplicationg.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.myapplicationg.ui.utils.BasePublishItem

@Composable
fun DefaultItemsOnLoading(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

fun <T : BasePublishItem> LazyListScope.pagingItems(
    items: LazyPagingItems<T>,
    itemOnLoading: (@Composable LazyItemScope.() -> Unit)? = null,
    itemsFooter: (@Composable LazyItemScope.(LoadState) -> Unit)? = null,
    onError: (LoadState.Error) -> Unit,
    content: @Composable LazyItemScope.(T?) -> Unit
) {
    if (items.loadState.refresh is LoadState.Loading) {
        item {
            if (itemOnLoading != null) itemOnLoading() else DefaultItemsOnLoading()
        }
    } else {
        if (items.loadState.refresh is LoadState.Error) {
            onError(items.loadState.refresh as LoadState.Error)
        }
        if (items.loadState.append is LoadState.Error) {
            onError(items.loadState.append as LoadState.Error)
        }
        items(
            items = items,
            key = { it.id },
            itemContent = content
        )
        item {
            if (itemsFooter != null) {
                itemsFooter(items.loadState.append)
            }
            else {
                DefaultItemsFooter(items.loadState.append)
            }
        }
    }
}

@Composable
fun DefaultItemsFooter(
    loadState: LoadState
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (loadState.endOfPaginationReached) {
            Text(
                text = "已加载全部动态",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            CircularProgressIndicator()
        }
    }
}