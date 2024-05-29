package com.lollipop.navigator2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun interface Navigator2 {

    fun navigate(page: String, args: NavIntentInfo?)
}

class PageInfo(
    val path: String,
    val args: NavIntentInfo
) {
    var state = PageState.INIT
}

typealias BackDispatcher = () -> Unit

typealias PageContent = @Composable (PaddingValues, Navigator2, NavIntent, BackDispatcher) -> Unit

@Composable
fun NavRoot(
    paddingValues: PaddingValues,
    initialPage: String,
    pageBackground: Color,
    register: ((String, PageContent) -> Unit) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pageMap = remember {
        SnapshotStateMap<String, PageContent>()
    }

    val pageStack = remember { SnapshotStateList<PageInfo>() }

    var isInit by remember { mutableStateOf(false) }

    val navigator2 = Navigator2 { page, args ->
        val pageInf = PageInfo(page, args ?: NavIntentInfo()).apply {
            if (page == initialPage && pageStack.isEmpty()) {
                state = PageState.START
            }
        }
        pageStack.add(pageInf)
    }

    if (!isInit) {
        val registerCallback = { key: String, page: PageContent ->
            pageMap[key] = page
        }
        register(registerCallback)
        navigator2.navigate(initialPage, null)
        isInit = true
    }

    pageStack.forEach { info ->
        val pageContent = pageMap[info.path]
        if (pageContent != null) {
            var isShown by mutableStateOf(false)
            when (info.state) {
                PageState.START -> {
                    isShown = true
                }

                PageState.STOP -> {
                    isShown = false
                }

                PageState.INIT -> {
                    isShown = false
                    coroutineScope.launch {
                        delay(10)
                        isShown = true
                        info.state = PageState.START
                    }
                }
            }
            AnimatedVisibility(
                visible = isShown,
                enter = slideIn {
                    IntOffset(it.width, 0)
                },
                exit = slideOut {
                    IntOffset(it.width, 0)
                }
            ) {
                Box(modifier = Modifier.fillMaxSize().background(pageBackground)) {
                    pageContent.invoke(
                        paddingValues,
                        navigator2,
                        NavIntent { info.args },
                        {
                            info.state = PageState.STOP
                            isShown = false
                        }
                    )
                }
            }
        }
    }

}

enum class PageState {
    INIT, START, STOP
}
