package com.lollipop.wte.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.lollipop.navigator2.BackDispatcher
import com.lollipop.wte.DataHelper
import com.lollipop.wte.local.Strings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MenuOutputPanel(paddingValues: PaddingValues, backDispatcher: BackDispatcher) {
    val dataHelper = DataHelper
    val clipboardManager = LocalClipboardManager.current
    var contentValue by mutableStateOf("")
    dataHelper.runWith { helper ->
        contentValue = withContext(Dispatchers.IO) {
            helper.toJson()
        }
    }
    ActionBarGroup(
        onBack = backDispatcher,
        actionButtons = {}
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            SelectionContainer(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = contentValue,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
            ExtendedFloatingActionButton(
                text = {
                    Text(Strings.current.copy)
                },
                onClick = {
                    clipboardManager.setText(AnnotatedString(contentValue))
                }
            )
        }
    }
}