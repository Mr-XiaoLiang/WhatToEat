package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.lollipop.navigator2.NavRoot
import com.lollipop.wte.router.Router
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    Initialize.init {
        DataHelper.coroutineScope = coroutineScope
        DataHelper.load()
    }

    val pageList = arrayOf(
        Router.Main,
        Router.Manager,
        Router.ItemAdd,
        Router.MenuInput,
        Router.MenuOutput
    )

    NavRoot(
        padding,
        Router.Main.path,
        Color.White
    ) { register ->
//        register(Router.Main.path, PageMode.Multiple) { padding, nav, intent, back ->
//            ContentPage(padding, nav)
//        }
        pageList.forEach {
            it.register(register)
        }
    }
}