package com.lollipop.wte.ui

import Config
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.lollipop.wte.DataHelper

@Composable
fun ContentPage(padding: PaddingValues, dataHelper: DataHelper) {
    MaterialTheme {
        ContentScaffold(
            padding,
            flagPanel = { padding, miniMode ->
                FlagPanel(padding, dataHelper, miniMode)
            },
            contentPanel = {
                MenuListPanel(padding, dataHelper)
            }
        )

//        var showContent by remember { mutableStateOf(false) }
//
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            Text("App Home: ${Platform.fileDir.path}")
//            AnimatedVisibility(showContent) {
//                Column(
//                    Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                }
//            }
//        }
    }

}

@Composable
fun ContentScaffold(
    padding: PaddingValues,
    flagPanel: @Composable (PaddingValues, miniMode: Boolean) -> Unit,
    contentPanel: @Composable (PaddingValues) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight
        val isHorizontal = maxWidth > maxHeight
        val isTablet = maxWidth > 480
        if (isHorizontal || isTablet) {
            ContentByTablet(padding, maxWidth, flagPanel, contentPanel)
        } else {
            ContentByPhone(padding, maxHeight, flagPanel, contentPanel)
        }
    }

}

@Composable
private fun ContentByTablet(
    padding: PaddingValues,
    maxWidth: Int,
    flagPanel: @Composable (PaddingValues, miniMode: Boolean) -> Unit,
    contentPanel: @Composable (PaddingValues) -> Unit,
) {
    var flagWidth = maxWidth * Config.FLAG_PANEL_WIDTH_WEIGHT
    if (flagWidth > Config.MAX_FLAG_PANEL_WIDTH) {
        flagWidth = Config.MAX_FLAG_PANEL_WIDTH.toFloat()
    }
    val contentWidth = maxWidth - flagWidth
    Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {

        Box(
            modifier = Modifier.width(flagWidth.dp).fillMaxHeight(),
        ) {
            flagPanel(
                PaddingValuesWrapper(
                    padding,
                    endEnable = false
                ),
                false
            )
        }

        Box(modifier = Modifier.width(contentWidth.dp).fillMaxHeight()) {
            contentPanel(
                PaddingValuesWrapper(
                    padding,
                    startEnable = false
                ),
            )
        }
    }
}

@Composable
private fun ContentByPhone(
    padding: PaddingValues,
    maxHeight: Int,
    flagPanel: @Composable (PaddingValues, miniMode: Boolean) -> Unit,
    contentPanel: @Composable (PaddingValues) -> Unit,
) {

    var flagHeight = maxHeight * Config.FLAG_PANEL_HEIGHT_WEIGHT
    if (flagHeight > Config.MAX_FLAG_PANEL_HEIGHT) {
        flagHeight = Config.MAX_FLAG_PANEL_HEIGHT.toFloat()
    }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {

        Box(
            modifier = Modifier.fillMaxWidth().height(flagHeight.dp),
        ) {
            flagPanel(
                PaddingValuesWrapper(
                    padding,
                    bottomEnable = false
                ),
                false
            )
        }

        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            contentPanel(
                PaddingValuesWrapper(
                    padding,
                    topEnable = false
                ),
            )
        }
    }
}

class PaddingValuesWrapper(
    private val basePadding: PaddingValues,
    private val startEnable: Boolean = true,
    private val topEnable: Boolean = true,
    private val endEnable: Boolean = true,
    private val bottomEnable: Boolean = true,
) : PaddingValues {
    override fun calculateBottomPadding(): Dp {
        return if (bottomEnable) {
            basePadding.calculateBottomPadding()
        } else {
            0.dp
        }
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        when (layoutDirection) {
            LayoutDirection.Ltr -> {
                return if (startEnable) {
                    basePadding.calculateLeftPadding(layoutDirection)
                } else {
                    0.dp
                }
            }

            LayoutDirection.Rtl -> {
                return if (endEnable) {
                    basePadding.calculateLeftPadding(layoutDirection)
                } else {
                    0.dp
                }
            }
        }
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        when (layoutDirection) {
            LayoutDirection.Ltr -> {
                return if (endEnable) {
                    basePadding.calculateRightPadding(layoutDirection)
                } else {
                    0.dp
                }
            }

            LayoutDirection.Rtl -> {
                return if (startEnable) {
                    basePadding.calculateRightPadding(layoutDirection)
                } else {
                    0.dp
                }
            }
        }
    }

    override fun calculateTopPadding(): Dp {
        return if (topEnable) {
            basePadding.calculateTopPadding()
        } else {
            0.dp
        }
    }

}
