package com.lollipop.navigator2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ExperimentalFoundationApi
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

class PageDefinition(
    val path: String,
    val mode: PageMode,
    val content: PageContent
)

class PageInfo(
    val path: String,
    val args: NavIntentInfo
) {
    var state = PageState.INIT

    fun clone(newState: PageState): PageInfo {
        val pageInfo = PageInfo(path, args)
        pageInfo.state = newState
        return pageInfo
    }

}

typealias BackDispatcher = () -> Unit

typealias PageContent = @Composable (PaddingValues, Navigator2, NavIntent, BackDispatcher) -> Unit

typealias PageRegister = (path: String, mode: PageMode, content: PageContent) -> Unit

private class Navigator2Impl(
    val initialPage: String,
    val pageMap: Map<String, PageDefinition>,
) : Navigator2 {

    val pageStack = SnapshotStateList<PageInfo>()

    private fun clearStoppedPage() {
        while (pageStack.isNotEmpty()) {
            val last = pageStack.last()
            if (last.state == PageState.STOP) {
                pageStack.removeLastOrNull()
            } else {
                // 倒着找，找不到停止的页面就停下
                break
            }
        }
    }

    override fun navigate(page: String, args: NavIntentInfo?) {
        // 执行之前做清理
        clearStoppedPage()
        val pageDefinition = pageMap[page]
        if (pageDefinition != null) {
            if (pageStack.isEmpty()) {
                val pageInfo = PageInfo(page, args ?: NavIntentInfo()).apply {
                    if (page == initialPage) {
                        state = PageState.START
                    }
                }
                pageStack.add(pageInfo)
            } else {
                when (pageDefinition.mode) {
                    PageMode.Multiple -> {
                        pageStack.add(PageInfo(page, args ?: NavIntentInfo()))
                    }

                    PageMode.Single -> {
                        val find = pageStack.find { it.path == page }
                        // 单例模式最难搞，需要找到已有的，然后把顶层的移除
                        if (find != null) {
                            val maxIndex = pageStack.size - 1
                            for (i in maxIndex downTo 0) {
                                val info = pageStack[i]
                                if (info.path != page) {
                                    // 现在不能移除，要做动画
                                    pageStack[i] = info.clone(PageState.STOP)
                                    println("close page = ${info.path}")
                                } else {
                                    break
                                }
                            }
                        } else {
                            pageStack.add(PageInfo(page, args ?: NavIntentInfo()))
                        }
                    }

                    PageMode.TopSingle -> {
                        val last = pageStack.last()
                        if (last.path != page) {
                            pageStack.add(PageInfo(page, args ?: NavIntentInfo()))
                        } else {
                            // 还是得移除再添加，否则列表不刷新
                            pageStack.remove(last)
                            pageStack.add(PageInfo(page, args ?: NavIntentInfo()).apply {
                                // 状态动画就不要再执行了
                                state = PageState.START
                            })
                        }
                    }
                }
            }
        }
        println("pageStack.size = ${pageStack.size}")
    }

}

@OptIn(ExperimentalFoundationApi::class)
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
        val registerCallback = { key: String, mode: PageMode, page: PageContent ->
            pageMap[key] = PageDefinition(key, mode, page)
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
                enter = slideIn {
                    IntOffset(it.width, 0)
                },
                exit = slideOut {
                    IntOffset(it.width, 0)
                }
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
                        {
                            info.state = PageState.STOP
                            isShown = false
                        }
                    )
                }
            }
            println("invoke page ${i} = ${info.path}")
        }
    }

}

enum class PageState {
    INIT, START, STOP
}
