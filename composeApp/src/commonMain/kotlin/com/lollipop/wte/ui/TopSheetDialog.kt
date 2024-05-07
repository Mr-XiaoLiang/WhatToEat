package com.lollipop.wte.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSheetDialog(
    show: Boolean,
    callClose: DialogInterface,
    content: @Composable BoxScope.(DialogInterface) -> Unit
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().combinedClickable(
                        enabled = true,
                    ) {
                        callClose.dismiss()
                    }
                )
                content(callClose)
            }
        }
    }
}

fun interface DialogInterface {
    fun dismiss()
}
