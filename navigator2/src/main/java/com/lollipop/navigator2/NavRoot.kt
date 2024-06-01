package com.lollipop.navigator2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun NavRoot(
    paddingValues: PaddingValues,
    initialPage: String,
    pageBackground: Color,
    register: (PageRegister) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val pageMap = remember { SnapshotStateMap<String, PageDefinition>() }

    val navigator2 = remember { Navigator2Impl(initialPage, pageMap) }

    val pageStack = remember { navigator2.pageStack }

    var isInit by remember { mutableStateOf(false) }

    if (!isInit) {
        val registerCallback = { page: PageDefinition ->
            pageMap[page.path] = page
        }
        register(registerCallback)
        navigator2.navigate(initialPage, null)
        isInit = true
    }

    val maxIndex = pageStack.size - 1
    var firstIndex = 0
    var shownPageCount = 0
    for (i in maxIndex downTo 0) {
        val info = pageStack[i]
        if (info.state == PageState.START) {
            shownPageCount++
        }
        if (shownPageCount > 1) {
            // 只保留最后2个可见的页面就好了
            firstIndex = i
            break
        }
    }
    for (i in 0..maxIndex) {
        val info = pageStack[i]
        val pageDefinition = pageMap[info.path]
        if (pageDefinition != null) {
            val pageContent = pageDefinition.content
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
            if (i < firstIndex) {
                continue
            }
            AnimatedVisibility(
                visible = isShown,
                enter = pageDefinition.enter,
                exit = pageDefinition.exit
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(pageBackground)
                        .clickable(
                            onClick = {
                                // 啥也不做，就是拦截点击事件
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                ) {
                    pageContent.invoke(
                        paddingValues,
                        navigator2,
                        NavIntent { info.args },
                    )
                }
            }
            Nav2Config.log("invoke page $i = ${info.path}")
        }
    }
}
