package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.lollipop.navigator.Navigator
import com.lollipop.navigator.NavigatorRoot
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
    Navigator.init {
        Navigator.register(Router.Main)
        Navigator.register(Router.Manager)
        Navigator.register(Router.ItemAdd)
        Navigator.register(Router.MenuInput)
        Navigator.register(Router.MenuOutput)
    }
    NavigatorRoot(padding)
}