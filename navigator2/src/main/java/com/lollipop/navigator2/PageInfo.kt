package com.lollipop.navigator2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

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

enum class PageState {
    INIT, START, STOP
}


typealias PageContent = @Composable (PaddingValues, Navigator2, NavIntent) -> Unit

typealias PageRegister = (path: String, mode: PageMode, content: PageContent) -> Unit

