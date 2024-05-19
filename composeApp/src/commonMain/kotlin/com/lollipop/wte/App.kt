package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.lollipop.navigator.NavigatorRoot
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    Initialize.init {
        DataHelper.coroutineScope = coroutineScope
        DataHelper.load()
    }
    NavigatorRoot(padding)
}