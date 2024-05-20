package com.lollipop.wte.ui

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
import androidx.compose.ui.Modifier
import com.lollipop.navigator.Navigator
import com.lollipop.navigator.PageScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import whattoeat.composeapp.generated.resources.Res
import whattoeat.composeapp.generated.resources.arrow_back_24dp

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PageScope.ActionBarGroup(
    modifier: Modifier = Modifier.fillMaxSize(),
    actionButtons: @Composable PageScope.() -> Unit,
    content: @Composable PageScope.() -> Unit,
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
                if (Navigator.canBack) {
                    IconButton(
                        onClick = {
                            back()
                        }
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