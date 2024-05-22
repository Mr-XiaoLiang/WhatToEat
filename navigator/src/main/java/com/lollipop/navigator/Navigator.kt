package com.lollipop.navigator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import java.util.LinkedList

object Navigator {

    private val routerMap = HashMap<String, PageInfo>()

    private val pageStack = LinkedList<PageScope>()

    val baseLayer = mutableStateOf<PageScope?>(null)

    val currentPage = mutableStateOf<PageScope?>(null)

    val destroyPage = mutableStateOf<PageScope?>(null)

    private var isInit = false

    var pageBackground: () -> Color = { Color.White }

    val canBack: Boolean
        get() {
            return pageStack.size > 1
        }

    fun init(block: () -> Unit) {
        if (isInit) {
            return
        }
        block()
        isInit = true
    }

    fun register(info: PageInfo) {
        val isFirst = this.routerMap.isEmpty()
        this.routerMap[info.path] = info
        // 先给他设置一个默认的，只设置一次
        if (isFirst && currentPage.value == null) {
            go(info.path)
        }
    }

    fun go(path: String, argument: IntentInfo? = null) {
        val pageInfo = routerMap[path] ?: return
        when (pageInfo.mode) {
            PageMode.Multiple -> {
                pushPage(PageScope(pageInfo).apply {
                    argument?.let { updateIntent(it) }
                })
            }

            PageMode.Single -> {
                val scope = pageStack.findLast { it.info.path == path } ?: PageScope(pageInfo)
                argument?.let { scope.updateIntent(it) }
                pushPage(scope)
            }

            PageMode.TopSingle -> {
                val last = optLastPage()
                if (last != null && last.info.path == path) {
                    argument?.let { last.updateIntent(it) }
                } else {
                    pushPage(PageScope(pageInfo).apply {
                        argument?.let { updateIntent(it) }
                    })
                }
            }
        }
    }

    fun finish(scope: PageScope) {
        val lastPage = optLastPage()
        pageStack.remove(scope)
        if (lastPage === scope) {
            destroyPage.value = scope
        }
        scope.isShown.value = false
        currentPage.value = optLastPage()
        baseLayer.value = optPreviousPage()
    }

    fun back(path: String) {
        routerMap[path] ?: return
        val last = optLastPage() ?: return
        if (last.info.path == path) {
            last.isShown.value = false
            destroyPage.value = last
            pageStack.removeLast()
            currentPage.value = optLastPage()
            baseLayer.value = optPreviousPage()
        }
    }

    fun backTo(path: String) {
        routerMap[path] ?: return
        val indexOfLast = pageStack.indexOfLast { it.info.path == path }
        if (indexOfLast < 0) {
            return
        }
        destroyPage.value = optLastPage()
        destroyPage.value?.isShown?.value = false
        val stackSize = indexOfLast + 1
        while (pageStack.size > stackSize) {
            pageStack.removeLast()
        }
        currentPage.value = optLastPage()
        baseLayer.value = optPreviousPage()
    }

    private fun optLastPage(): PageScope? {
        if (pageStack.isNotEmpty()) {
            return pageStack.last()
        }
        return null
    }

    private fun optPreviousPage(): PageScope? {
        if (pageStack.size > 1) {
            return pageStack[pageStack.size - 2]
        }
        return null
    }

    private fun pushPage(pageScope: PageScope) {
        baseLayer.value = currentPage.value
        pageScope.isShown.value = true
        pageStack.addLast(pageScope)
        currentPage.value = pageScope
        destroyPage.value = null
    }

}

@Composable
fun NavigatorRoot(padding: PaddingValues) {
    val currentPage by remember { Navigator.currentPage }
    val baseLayer by remember { Navigator.baseLayer }
    val destroyLayer by remember { Navigator.destroyPage }
    baseLayer?.let {
        it.padding = padding
        it.NavigatorAnimatedVisibility(it.isShown) {
            it.compose()
        }
    }
    currentPage?.let {
        it.padding = padding
        it.NavigatorAnimatedVisibility(it.isShown) {
            it.compose()
        }
    }
    destroyLayer?.let {
        it.padding = padding
        it.NavigatorAnimatedVisibility(it.isShown) {
            it.compose()
        }
    }
}

@Composable
private fun PageScope.NavigatorAnimatedVisibility(
    showState: MutableState<Boolean>,
    content: @Composable PageScope.() -> Unit
) {
    val isShow by remember { showState }
    AnimatedVisibility(
        visible = isShow,
        enter = slideIn {
            IntOffset(-it.width, 0)
        },
        exit = slideOut {
            IntOffset(-it.width, 0)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Navigator.pageBackground())) {
            content()
        }
    }
}

fun Navigate(
    path: String,
    pageMode: PageMode = PageMode.Multiple,
    content: @Composable PageScope.() -> Unit
): PageInfo {
    val pageInfo = SinglePageInfo(path, pageMode, content)
    Navigator.register(pageInfo)
    return pageInfo
}
