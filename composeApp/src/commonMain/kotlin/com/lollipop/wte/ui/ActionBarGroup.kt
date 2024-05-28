package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lollipop.navigator.Navigator
import com.lollipop.navigator.PageScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import whattoeat.composeapp.generated.resources.Res
import whattoeat.composeapp.generated.resources.arrow_back_24dp

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ActionBarGroup(
    isShowBack: Boolean = true,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxSize().background(Color.White),
    actionButtons: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isShowBack) {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(painterResource(Res.drawable.arrow_back_24dp), "")
                    }
                }
                Spacer(modifier = Modifier.weight(1F))
                actionButtons()
            }
        }
        content()
    }

}