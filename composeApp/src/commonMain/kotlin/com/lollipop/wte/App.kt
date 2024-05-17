package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.lollipop.navigator.Navigator
import com.lollipop.wte.ui.ContentPage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    val dataHelper = remember { DataHelper(coroutineScope) }
    Initialize.init {
        dataHelper.load()
    }
    val currentPage by remember { Navigator.currentPage }
    Navigator.init {
        // TODO
    }
    currentPage?.compose()

    // ContentPage(padding, dataHelper)
}