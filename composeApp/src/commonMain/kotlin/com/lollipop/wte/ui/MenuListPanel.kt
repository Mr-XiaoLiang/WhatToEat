package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lollipop.wte.DataHelper

@Composable
fun MenuListPanel(padding: PaddingValues, data: DataHelper) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
//        val maxWidth = constraints.maxWidth
        maxWidth
    }
    // TODO
    Text("Content", modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Green))

}