package com.lollipop.navigator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

class PageScope(
    val info: PageInfo,
) {

    val intent = HashMap<String, Any>()

    val isShown = mutableStateOf(false)

    var padding = PaddingValues(0.dp)

    fun updateIntent(argument: IntentInfo) {
        intent.putAll(argument.data)
    }

    inline fun <reified T : IntentInfo> T.sync(): T {
        val info = this
        info.update(intent)
        return info
    }

    @Composable
    fun compose() {
        info.content(this)
//        val showState by isShown
//        val pageContext = this
//        val scope = remember { pageContext }
//        AnimatedVisibility(
//            visible = showState,
//            enter = slideIn {
//                IntOffset(it.width, 0)
//            },
//            exit = slideOut {
//                IntOffset(it.width, 0)
//            }
//        ) {
//            Box(modifier = Modifier.fillMaxSize().background(Navigator.pageBackground())) {
//                info.content(scope)
//            }
//        }
    }

    fun back() {
//        Navigator.back(info.path)
        Navigator.back("")
    }

    fun go(path: String, argument: IntentInfo?) {
        Navigator.go(path, argument)
    }

}