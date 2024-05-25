package com.lollipop.navigator

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.LinkedList

object Navigator {

    private val routerMap = HashMap<String, PageInfo>()

    val pageStack = LinkedList<PageScope>()

    val baseLayer = mutableStateOf<PageScope?>(null)

    val currentPage = mutableStateOf<PageScope?>(null)

    val destroyPage = mutableStateOf<PageScope?>(null)

    private var isInit = false

    var pageBackground: () -> Color = { Color.White }

    internal var coroutineScope: CoroutineScope? = null

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
        currentPage.value = optLastPage()
        baseLayer.value = optPreviousPage()
        scope.changeShownState(false)
    }

    fun back(path: String) {
        if (path.isEmpty()) {
            val scope = currentPage.value
            if (scope != null) {
                finish(scope)
            }
            return
        }
        routerMap[path] ?: return
        val last = optLastPage() ?: return
        if (last.info.path == path) {
            destroyPage.value = last
            pageStack.removeLast()
            currentPage.value = optLastPage()
            baseLayer.value = optPreviousPage()
            last.changeShownState(false)
        }
    }

    fun backTo(path: String) {
        routerMap[path] ?: return
        val indexOfLast = pageStack.indexOfLast { it.info.path == path }
        if (indexOfLast < 0) {
            return
        }
        destroyPage.value = optLastPage()

        val stackSize = indexOfLast + 1
        while (pageStack.size > stackSize) {
            pageStack.removeLast()
        }
        currentPage.value = optLastPage()
        baseLayer.value = optPreviousPage()
        destroyPage.value?.changeShownState(false)
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
        pageStack.addLast(pageScope)
        currentPage.value = pageScope
        destroyPage.value = null
        pageScope.changeShownState(true)
    }

    private fun post(runnable: suspend () -> Unit) {
        coroutineScope?.launch {
            runnable()
        }
    }

}

@Composable
fun NavigatorRoot(padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    Navigator.coroutineScope = coroutineScope
    val currentPage by remember { Navigator.currentPage }
    val baseLayer by remember { Navigator.baseLayer }
    val destroyLayer by remember { Navigator.destroyPage }
    baseLayer?.let {
        it.padding = padding
        var isShow by remember { mutableStateOf(it.getShownState()) }
        it.Compose(isShow) {
            isShow = it.getShownState()
        }
    }
    currentPage?.let {
        it.padding = padding
        var isShow by remember { mutableStateOf(it.getShownState()) }
        it.Compose(isShow) {
            isShow = it.getShownState()
        }
    }
    destroyLayer?.let {
        it.padding = padding
        var isShow by remember { mutableStateOf(it.getShownState()) }
        it.Compose(isShow) {
            isShow = it.getShownState()
        }
    }
}

@Composable
fun Navigate(
    path: String,
    pageMode: PageMode = PageMode.Multiple,
    content: @Composable PageScope.() -> Unit
): PageInfo {
    val pageInfo = SinglePageInfo(path, pageMode, content)
    Navigator.register(pageInfo)
    return pageInfo
}
