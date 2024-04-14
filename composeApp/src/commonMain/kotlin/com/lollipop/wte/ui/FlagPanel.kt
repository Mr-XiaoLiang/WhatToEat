package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lollipop.wte.DataHelper

@Composable
fun FlagPanel(padding: PaddingValues, data: DataHelper, miniMode: Boolean) {

    // TODO

    Text("Flag", modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red))

}