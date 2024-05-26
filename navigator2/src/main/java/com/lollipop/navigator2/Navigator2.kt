package com.lollipop.navigator2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap

fun interface Navigator2 {

    fun navigate(page: String, args: Map<String, Any>?)
}

fun interface NavIntent {

    fun intent(): Map<String, Any>
}

class PageInfo(
    val path: String,
    val args: Map<String, Any>
)

typealias PageContent = @Composable (PaddingValues, Navigator2, NavIntent) -> Unit

@Composable
fun NavRoot(
    paddingValues: PaddingValues,
    initialPage: String,
    register: ((String, PageContent) -> Unit) -> Unit,
) {

    val pageMap = remember {
        SnapshotStateMap<String, PageContent>()
    }

    val pageStack = remember { SnapshotStateList<PageInfo>() }

    var isInit by remember { mutableStateOf(false) }

    val navigator2 = Navigator2 { page, args ->
        pageStack.add(PageInfo(page, args ?: emptyMap()))
    }

    if (!isInit) {
        val registerCallback = { key: String, page: PageContent ->
            pageMap[key] = page
        }
        register(registerCallback)
        navigator2.navigate(initialPage, null)
        isInit = true
    }

    pageStack.forEach {
        pageMap[it.path]?.invoke(paddingValues, navigator2, NavIntent { it.args })
    }

}
