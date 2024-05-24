package com.lollipop.navigator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val shownStateMap = SnapshotStateMap<String, Boolean>()

class PageScope(
    val info: PageInfo,
) {

    val pageId: String by lazy {
        System.identityHashCode(this@PageScope).toString(16)
    }

    val intent = HashMap<String, Any>()

    var padding = PaddingValues(0.dp)

    private var onShownStateChanged: (() -> Unit)? = null

    fun updateIntent(argument: IntentInfo) {
        intent.putAll(argument.data)
    }

    fun changeShownState(state: Boolean) {
//        isShownState.value = state
        shownStateMap[pageId] = state
        onShownStateChanged?.invoke()
        Navigator.coroutineScope?.launch {
            delay(10)
            onShownStateChanged?.invoke()
            println("$pageId:${onShownStateChanged} ==> changeShownState: $state")
        }
    }

    fun getShownState(): Boolean {
        val b = shownStateMap[pageId]
        if (b == null) {
            changeShownState(false)
            return false
        }
        return b
    }

    inline fun <reified T : IntentInfo> T.sync(): T {
        val info = this
        info.update(intent)
        return info
    }

    @Composable
    fun Compose(
        isShown: Boolean,
        onShownStateChanged: () -> Unit
    ) {
        this.onShownStateChanged = onShownStateChanged
//        val stateMap = remember { shownStateMap }
//        val showState = stateMap[pageId] ?: false
        println("${info.path} ==> ${isShown}")
        val pageContext = this
        AnimatedVisibility(
            visible = isShown,
            enter = slideIn {
                IntOffset(it.width, 0)
            },
            exit = slideOut {
                IntOffset(it.width, 0)
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Navigator.pageBackground())) {
                info.content(pageContext)
            }
        }
    }

    fun back() {
        Navigator.back(info.path)
    }

    @Composable
    fun go(path: String, argument: IntentInfo?) {
        Navigator.go(path, argument)
    }

}