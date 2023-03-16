package com.example.myapplicationg.ui.page.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplicationg.ui.page.profile.Profile
import com.example.myapplicationg.ui.widgets.PostsList
import kotlinx.coroutines.launch

sealed class MainPage(val name: String, val icon: ImageVector) {
    object Posts : MainPage("mainPage", Icons.Filled.Home)
    object Profile : MainPage("profile", Icons.Filled.AccountCircle)
}

data class NavItem(
    val onClick: () -> Unit,
    val screen: MainPage
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainPage(
    state: MainPageUiState,
    onLikeChanged: (postId: Int) -> Unit,
    openPost: (postId: Int) -> Unit,
    showNoNetWorkBar: (LoadState.Error) -> Unit,
    snackBarShown: () -> Unit,
    publish: (String) -> Unit,
) {
    val lazyPagingPosts = state.pager.collectAsLazyPagingItems()

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    if (state.noNet) {
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar("当前无网络")
            snackBarShown()
        }
    }
    var text by remember {
        mutableStateOf("")
    }
    if (state.postSent) {
        LaunchedEffect(snackBarHostState) {
            bottomState.hide()
            text = ""
            lazyPagingPosts.refresh()
            snackBarHostState.showSnackbar("已发送")

            snackBarShown()
        }
    }

    var selectedItem by remember {
        mutableStateOf(MainPage.Posts as MainPage)
    }

    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .weight(5F)
                        .heightIn(50.dp, 200.dp)
                        .padding(10.dp)
                )
                TextButton(
                    onClick = { publish(text) },
                    modifier = Modifier.weight(1F)
                ) {
                    Text(text = "发送")
                }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { coroutineScope.launch { bottomState.show() } },
                    expanded = expandedFab,
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text(text = "发动态") },
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                BottomBar(
                    onClickMainPage = { selectedItem = MainPage.Posts },
                    onClickProfile = { selectedItem = MainPage.Profile },
                    selected = selectedItem.name
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { paddingValues ->
            Surface(
                Modifier.padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                when (selectedItem) {
                    MainPage.Posts -> PostsList(
                        posts = lazyPagingPosts,
                        onLikeChanged = onLikeChanged,
                        openComment = openPost,
                        listState = listState,
                        noNetworkError = showNoNetWorkBar
                    )

                    MainPage.Profile -> Profile(state = state.profileState)
                }
            }
        }
    }

}

//@Preview
@Composable
fun BottomBar(
    selected: String,
    onClickMainPage: () -> Unit = {},
    onClickProfile: () -> Unit = {},
) {
    val items = listOf(
        NavItem(onClickMainPage, MainPage.Posts),
        NavItem(onClickProfile, MainPage.Profile)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.screen.icon, contentDescription = null) },
                selected = selected == item.screen.name,
                onClick = item.onClick,
            )

        }
    }
}


