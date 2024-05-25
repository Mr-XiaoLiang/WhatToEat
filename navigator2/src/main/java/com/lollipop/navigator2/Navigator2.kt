package com.lollipop.navigator2

import androidx.compose.runtime.Composable

private val routerMap = HashMap<String, PageInfo>()

class PageInfo(
    val pagePath: String,
    val pageContext: @Composable () -> Unit
)

interface Navigator2 {
}