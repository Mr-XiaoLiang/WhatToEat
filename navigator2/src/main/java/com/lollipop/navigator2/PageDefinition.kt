package com.lollipop.navigator2

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

class PageDefinition(
    val path: String,
    val mode: PageMode,
    val enter: EnterTransition = slideIn {
        IntOffset(it.width, 0)
    },
    val exit: ExitTransition = slideOut {
        IntOffset(it.width, 0)
    },
    val content: PageContent
)