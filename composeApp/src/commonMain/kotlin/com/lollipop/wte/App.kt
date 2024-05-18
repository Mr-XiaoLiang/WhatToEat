package com.lollipop.wte

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.lollipop.navigator.Navigator
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
    val currentPage by remember { Navigator.currentPage }

    currentPage?.let {
        it.padding = padding
        it.compose()
    }

    // ContentPage(padding, dataHelper)
}