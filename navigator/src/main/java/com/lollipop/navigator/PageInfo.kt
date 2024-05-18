package com.lollipop.navigator

import androidx.compose.runtime.Composable

interface PageInfo {
    val path: String
    val mode: PageMode
    val content: @Composable PageScope.() -> Unit
}

class SinglePageInfo(
    override val path: String,
    override val mode: PageMode,
    override val content: @Composable PageScope.() -> Unit
): PageInfo