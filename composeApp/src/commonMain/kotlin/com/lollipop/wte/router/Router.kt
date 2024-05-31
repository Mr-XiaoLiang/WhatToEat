package com.lollipop.wte.router

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.lollipop.navigator2.NavIntent
import com.lollipop.navigator2.NavIntentInfo
import com.lollipop.navigator2.Navigator2
import com.lollipop.navigator2.PageMode
import com.lollipop.navigator2.PageRegister
import com.lollipop.wte.ui.ContentPage
import com.lollipop.wte.ui.ItemAddPanel
import com.lollipop.wte.ui.ManagerPanel
import com.lollipop.wte.ui.MenuInputPanel
import com.lollipop.wte.ui.MenuOutputPanel

sealed class Router {

    open val path: String
        get() {
            return this::class.java.name
        }

    open val mode: PageMode = PageMode.Multiple

    protected inline fun <reified T : NavIntentInfo> goWith(
        navigator2: Navigator2,
        argumentBuild: T.() -> Unit
    ) {
        navigator2.navigate(path, T::class.java.getConstructor().newInstance().apply(argumentBuild))
    }

    protected fun goWithPath(navigator2: Navigator2) {
        navigator2.navigate(path, null)
    }

    fun register(register: PageRegister) {
        register.invoke(path, mode) { p, n, i ->
            pageContent(p, n, i)
        }
    }

    @Composable
    abstract fun pageContent(
        padding: PaddingValues,
        navigator2: Navigator2,
        intent: NavIntent,
    )

    data object Main : Router() {

        override val mode: PageMode = PageMode.Single

        fun go(navigator2: Navigator2) {
            goWithPath(navigator2)
        }

        @Composable
        override fun pageContent(
            padding: PaddingValues,
            navigator2: Navigator2,
            intent: NavIntent,
        ) {
            ContentPage(padding, navigator2)
        }

    }

    data object Manager : Router() {

        fun go(navigator2: Navigator2) {
            goWithPath(navigator2)
        }

        @Composable
        override fun pageContent(
            padding: PaddingValues,
            navigator2: Navigator2,
            intent: NavIntent,
        ) {
            ManagerPanel(padding, navigator2)
        }

    }

    data object ItemAdd : Router() {
        class Intent : NavIntentInfo() {

            var nameValue by self("")

        }

        fun go(
            navigator2: Navigator2,
            argumentBuild: Intent.() -> Unit
        ) {
            goWith(navigator2, argumentBuild)
        }

        @Composable
        override fun pageContent(
            padding: PaddingValues,
            navigator2: Navigator2,
            intent: NavIntent,
        ) {
            ItemAddPanel(padding, navigator2, intent)
        }

    }

    data object MenuInput : Router() {

        fun go(navigator2: Navigator2) {
            goWithPath(navigator2)
        }

        @Composable
        override fun pageContent(
            padding: PaddingValues,
            navigator2: Navigator2,
            intent: NavIntent,
        ) {
            MenuInputPanel(padding, navigator2)
        }
    }

    data object MenuOutput : Router() {

        fun go(navigator2: Navigator2) {
            goWithPath(navigator2)
        }

        @Composable
        override fun pageContent(
            padding: PaddingValues,
            navigator2: Navigator2,
            intent: NavIntent,
        ) {
            MenuOutputPanel(padding, navigator2)
        }

    }

}