package com.lollipop.navigator

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
    }

    fun back() {
        Navigator.back(info.path)
    }

    fun go(path: String, argument: IntentInfo?) {
        Navigator.go(path, argument)
    }

}