package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.wte.Config
import com.lollipop.wte.DataHelper
import com.lollipop.wte.local.Strings

@Composable
fun ContentPage(padding: PaddingValues, dataHelper: DataHelper) {

    var showTagDialog by remember { mutableStateOf(false) }
    var showTagFilter by remember { mutableStateOf(false) }
    val selectorList = remember { dataHelper.selectTagList }

    var showManagerPanel by remember { mutableStateOf(false) }

    MaterialTheme {
        ContentScaffold(
            padding,
            flagPanel = { padding, miniMode ->
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                showManagerPanel = true
                            },
                            modifier = Modifier.width(48.dp).height(48.dp).padding(12.dp)
                        ) {
                            Icon(Icons.Default.Settings, "Settings", tint = LColor.onBackground)
                        }
                    }
                    FlagPanel(padding, miniMode, dataHelper) {
                        if (!showTagDialog) {
                            showTagDialog = true
                        }
                    }
                }
                showTagFilter = miniMode
            },
            contentPanel = {
                MenuListPanel(padding, dataHelper)
            }
        )

        if (showTagFilter && selectorList.isNotEmpty()) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val top = maxHeight * Config.FLAG_PANEL_HEIGHT_WEIGHT
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 0.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Box(modifier = Modifier.height(top - 28.dp).fillMaxWidth())
                    ExtendedFloatingActionButton(
                        text = {
                            Text(text = Strings.current.selectNewTag)
                        },
                        onClick = { showTagDialog = true }
                    )
                }
            }
        }

        TopSheetDialog(show = showTagDialog, callClose = { showTagDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8F)
            ) {
                FlagPanel(padding, false, dataHelper) {}
            }
        }

        TopSheetDialog(
            show = showManagerPanel,
            callClose = { showManagerPanel = false }
        ) {
            ManagerPanel(padding, dataHelper)
        }

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
        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(LColor.background)
    ) {
        val isHorizontal = maxWidth > maxHeight
        val isTablet = maxWidth > Config.MIN_TABLET_WIDTH.dp
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
    maxWidth: Dp,
    flagPanel: @Composable (PaddingValues, miniMode: Boolean) -> Unit,
    contentPanel: @Composable (PaddingValues) -> Unit,
) {
    var flagWidth = (maxWidth * Config.FLAG_PANEL_WIDTH_WEIGHT)
    val maxFlagWidth = Config.MAX_FLAG_PANEL_WIDTH.dp
    if (flagWidth > maxFlagWidth) {
        flagWidth = maxFlagWidth
    }
    val contentWidth = maxWidth - flagWidth
    Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {

        Box(
            modifier = Modifier.width(flagWidth).fillMaxHeight(),
        ) {
            flagPanel(
                padding.wrapperOf(endEdge = PaddingValuesWrapper.Edge.Disable),
                false
            )
        }

        Box(modifier = Modifier.width(contentWidth).fillMaxHeight()) {
            contentPanel(
                padding.wrapperOf(startEdge = PaddingValuesWrapper.Edge.Disable)
            )
        }
    }
}

@Composable
private fun ContentByPhone(
    padding: PaddingValues,
    maxHeight: Dp,
    flagPanel: @Composable (PaddingValues, miniMode: Boolean) -> Unit,
    contentPanel: @Composable (PaddingValues) -> Unit,
) {

    var flagHeight = maxHeight * Config.FLAG_PANEL_HEIGHT_WEIGHT
    val maxFlagHeight = Config.MAX_FLAG_PANEL_HEIGHT.dp
    if (flagHeight > maxFlagHeight) {
        flagHeight = maxFlagHeight
    }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {

        Box(
            modifier = Modifier.fillMaxWidth().height(flagHeight),
        ) {
            flagPanel(
                padding.wrapperOf(bottomEdge = PaddingValuesWrapper.Edge.Disable),
                true
            )
        }

        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            contentPanel(
                padding.wrapperOf(topEdge = PaddingValuesWrapper.Edge.Disable)
            )
        }
    }
}
