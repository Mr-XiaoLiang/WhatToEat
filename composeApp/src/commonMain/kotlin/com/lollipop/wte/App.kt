package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.lollipop.navigator2.NavRoot
import com.lollipop.wte.router.Router
import com.lollipop.wte.ui.ContentPage
import com.lollipop.wte.ui.ItemAddPanel
import com.lollipop.wte.ui.ManagerPanel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    Initialize.init {
        DataHelper.coroutineScope = coroutineScope
        DataHelper.load()
    }
//    Navigator.init {
//        Navigator.register(Router.Main)
//        Navigator.register(Router.Manager)
//        Navigator.register(Router.ItemAdd)
//        Navigator.register(Router.MenuInput)
//        Navigator.register(Router.MenuOutput)
//    }
//    NavigatorRoot(padding)

    NavRoot(
        padding,
        Router.Main.path,
        Color.White
    ) { register ->
        register(Router.Main.path) { padding, nav, intent, back ->
            ContentPage(padding, nav)
        }
        register(Router.Manager.path) { padding, nav, intent, back ->
            ManagerPanel(padding, nav, back)
        }
        register(Router.ItemAdd.path) { padding, nav, intent, back ->
            ItemAddPanel(padding, nav, intent, back)
        }
    }
}