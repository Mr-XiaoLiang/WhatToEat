package com.lollipop.wte.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSheetDialog(
    show: Boolean,
    callClose: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    val dialogBackground by animateColorAsState(
        if (show) {
            Color(0x33000000)
        } else {
            Color.Transparent
        }
    )

    Column(
        modifier = Modifier.fillMaxSize().background(dialogBackground)
    ) {
        AnimatedVisibility(visible = show) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Box(
                    modifier = Modifier.fillMaxSize().combinedClickable(
                        enabled = true,
                    ) {
                        callClose()
                    }
                )
            }
        }
    }
}