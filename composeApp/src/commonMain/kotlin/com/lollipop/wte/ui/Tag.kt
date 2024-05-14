package com.lollipop.wte.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tag(text: String, isSelect: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .combinedClickable {
                    onClick()
                },
        ) {
            Text(
                modifier = Modifier.wrapContentWidth().wrapContentHeight()
                    .background(getItemBackgroundColor(isSelect))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                text = text,
                textAlign = TextAlign.Center,
                color = getItemContentColor(isSelect),
            )
        }
    }
}

private fun getItemBackgroundColor(selected: Boolean): Color {
    return if (selected) {
        LColor.themeColor
    } else {
        LColor.background
    }
}

private fun getItemContentColor(selected: Boolean): Color {
    return if (selected) {
        LColor.onThemeColor
    } else {
        LColor.onBackground
    }
}