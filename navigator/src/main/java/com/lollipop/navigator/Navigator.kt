package com.lollipop.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import java.util.LinkedList

object Navigator {

    private val routerMap = HashMap<String, PageInfo>()

    private val pageStack = LinkedList<PageScope>()

    var currentPage = mutableStateOf<PageScope?>(null)

    private var isInit = false

    fun init(block: () -> Unit) {
        if (isInit) {
            return
        }
        block()
        isInit = true
    }

    // TODO

}

class PageScope(
    val info: PageInfo,
) {

    val intent = HashMap<String, Any>()

    inline fun <reified T : IntentInfo> T.sync(): T {
        val info = this
        info.update(intent)
        return info
    }

    @Composable
    fun compose() {
        info.content(this)
    }

}

class PageInfo(
    val path: String,
    val content: @Composable PageScope.() -> Unit
)


