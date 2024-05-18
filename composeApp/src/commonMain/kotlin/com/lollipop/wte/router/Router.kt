package com.lollipop.wte.router

import androidx.compose.runtime.Composable
import com.lollipop.navigator.IntentInfo
import com.lollipop.navigator.Navigator
import com.lollipop.navigator.PageInfo
import com.lollipop.navigator.PageMode
import com.lollipop.navigator.PageScope
import com.lollipop.wte.ui.ContentPage

sealed class Router<T : IntentInfo> : PageInfo {

    override val path: String
        get() {
            return this::class.java.name
        }

    override val mode: PageMode = PageMode.Multiple

    abstract fun go(argumentBuild: T.() -> Unit = {})

    protected fun go(info: IntentInfo) {
        Navigator.go(path, info)
    }

    data object Main : Router<Main.Intent>() {

        class Intent : IntentInfo()

        override fun go(argumentBuild: Intent.() -> Unit) {
            go(Intent().apply(argumentBuild))
        }

        override val content: @Composable PageScope.() -> Unit
            get() = { ContentPage() }

    }

}