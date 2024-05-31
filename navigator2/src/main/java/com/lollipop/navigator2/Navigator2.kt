package com.lollipop.navigator2

import androidx.compose.runtime.snapshots.SnapshotStateList

interface Navigator2 {
    fun navigate(page: String, args: NavIntentInfo?)

    fun back()
}

class Navigator2Impl(
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
                                    Nav2Config.log("close page = ${info.path}")
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
        Nav2Config.log("pageStack.size = ${pageStack.size}")
    }

    override fun back() {
        // 清理一下已经停止的页面
        clearStoppedPage()
        // 如果已经没有可用页面，就算了吧
        if (pageStack.size < 2) {
            // 都没了，就别退了
            return
        }
        // 倒着找，找到最后一个可见的页面，给它关掉
        val maxIndex = pageStack.size - 1
        for (i in maxIndex downTo 0) {
            val info = pageStack[i]
            if (info.state != PageState.STOP) {
                // 如果不是停止状态，说明是活跃的，那么这才是有效的
                if (i < 1) {
                    // 都是第一个了，退啥呀
                    return
                }
                // 现在不能移除，要做动画
                pageStack[i] = info.clone(PageState.STOP)
                Nav2Config.log("close page = ${info.path}")
                break
            }
        }
    }

}