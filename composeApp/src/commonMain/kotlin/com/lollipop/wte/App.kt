package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.lollipop.navigator2.Nav2Config
import com.lollipop.navigator2.NavRoot
import com.lollipop.wte.router.Router
import logger
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    Initialize.init {
        Nav2Config.logger = logger()
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
        pageList.forEach {
            it.register(register)
        }
    }
}