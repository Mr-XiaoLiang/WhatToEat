package com.lollipop.wte.router

import androidx.compose.runtime.Composable
import com.lollipop.navigator.IntentInfo
import com.lollipop.navigator.Navigator
import com.lollipop.navigator.PageInfo
import com.lollipop.navigator.PageMode
import com.lollipop.navigator.PageScope
import com.lollipop.navigator2.NavIntentInfo
import com.lollipop.wte.ui.MenuInputPanel
import com.lollipop.wte.ui.MenuOutputPanel

sealed class Router : PageInfo {

    override val path: String
        get() {
            return this::class.java.name
        }

    override val mode: PageMode = PageMode.Multiple

    protected inline fun <reified T : IntentInfo> goWith(argumentBuild: T.() -> Unit) {
        Navigator.go(path, T::class.java.getConstructor().newInstance().apply(argumentBuild))
    }

    protected fun goWithPath() {
        Navigator.go(path, null)
    }

    data object Main : Router() {

        fun go() {
            goWithPath()
        }

        override val content: @Composable PageScope.() -> Unit
            get() = {
//                ContentPage()
            }

    }

    data object Manager : Router() {

        fun go() {
            goWithPath()
        }

        override val content: @Composable PageScope.() -> Unit
            get() = {
//                ManagerPanel()
            }

    }

    data object ItemAdd : Router() {
        class Intent : NavIntentInfo() {

            var nameValue by self("")

        }

        override val content: @Composable PageScope.() -> Unit
            get() = {
                // ItemAddPanel()
            }

    }

    data object MenuInput : Router() {

        fun go() {
            goWithPath()
        }

        override val content: @Composable PageScope.() -> Unit
            get() = { MenuInputPanel() }
    }

    data object MenuOutput : Router() {

        fun go() {
            goWithPath()
        }

        override val content: @Composable PageScope.() -> Unit
            get() = { MenuOutputPanel() }
    }

}