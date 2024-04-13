package com.lollipop.wte.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.lollipop.wte.DataHelper
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ContentPage(padding: PaddingValues, dataHelper: DataHelper) {
    MaterialTheme {
        ContentScaffold(padding, dataHelper)

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
fun ContentScaffold(padding: PaddingValues, dataHelper: DataHelper) {

}
