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
    if (miniMode) {
        FlagPanelByPhone(padding, data)
    } else {
        FlagPanelByTablet(padding, data)
    }
}

@Composable
private fun FlagPanelByPhone(padding: PaddingValues, data: DataHelper) {
    Text("FlagPanelByPhone", modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red))
    //TODO()
}

@Composable
private fun FlagPanelByTablet(padding: PaddingValues, data: DataHelper) {
    Text("FlagPanelByTablet", modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red))
}
