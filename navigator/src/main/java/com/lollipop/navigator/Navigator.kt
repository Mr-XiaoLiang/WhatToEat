package com.lollipop.navigator

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.LinkedList

object Navigator {

    private val routerMap = HashMap<String, PageInfo>()

    private val pageStack = LinkedList<PageScope>()

    val currentPage = mutableStateOf<PageScope?>(null)

    private var isInit = false

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
        if (lastPage === scope) {
            pageStack.removeLast()
            currentPage.value = optLastPage()
        } else {
            pageStack.remove(scope)
        }
    }

    fun back(path: String) {
        routerMap[path] ?: return
        val last = optLastPage() ?: return
        if (last.info.path == path) {
            pageStack.removeLast()
            currentPage.value = optLastPage()
        }
    }

    fun backTo(path: String) {
        routerMap[path] ?: return
        val indexOfLast = pageStack.indexOfLast { it.info.path == path }
        if (indexOfLast < 0) {
            return
        }
        val stackSize = indexOfLast + 1
        while (pageStack.size > stackSize) {
            pageStack.removeLast()
        }
        currentPage.value = optLastPage()
    }

    private fun optLastPage(): PageScope? {
        if (pageStack.isNotEmpty()) {
            return pageStack.last()
        }
        return null
    }

    private fun pushPage(pageScope: PageScope) {
//        val last = optLastPage()
        pageStack.addLast(pageScope)
        currentPage.value = pageScope
        // 动画先不写
    }

}

@Composable
fun NavigatorRoot(padding: PaddingValues) {
    val currentPage by remember { Navigator.currentPage }
    currentPage?.let {
        it.padding = padding
        it.compose()
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
