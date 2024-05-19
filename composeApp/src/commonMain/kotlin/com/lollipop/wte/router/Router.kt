package com.lollipop.wte.router

import androidx.compose.runtime.Composable
import com.lollipop.navigator.IntentInfo
import com.lollipop.navigator.Navigator
import com.lollipop.navigator.PageInfo
import com.lollipop.navigator.PageMode
import com.lollipop.navigator.PageScope
import com.lollipop.wte.ui.ContentPage
import com.lollipop.wte.ui.ManagerPanel

sealed class Router<T : IntentInfo> : PageInfo {

    override val path: String
        get() {
            return this::class.java.name
        }

    override val mode: PageMode = PageMode.Multiple

    abstract fun go(argumentBuild: T.() -> Unit = {})

    protected inline fun <reified T : IntentInfo> goWith(argumentBuild: T.() -> Unit) {
        Navigator.go(path, T::class.java.getConstructor().newInstance().apply(argumentBuild))
    }

    data object Main : Router<Main.Intent>() {

        class Intent : IntentInfo()

        override fun go(argumentBuild: Intent.() -> Unit) {
            goWith<Intent>(argumentBuild)
        }

        override val content: @Composable PageScope.() -> Unit
            get() = { ContentPage() }

    }

    data object Manager : Router<Manager.Intent>() {
        class Intent : IntentInfo()

        override fun go(argumentBuild: Intent.() -> Unit) {
            goWith<Intent>(argumentBuild)
        }

        override val content: @Composable PageScope.() -> Unit
            get() = { ManagerPanel() }

    }

}