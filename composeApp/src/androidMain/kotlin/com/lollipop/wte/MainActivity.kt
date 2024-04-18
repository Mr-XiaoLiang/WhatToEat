package com.lollipop.wte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                App(innerPadding)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        App(innerPadding)
    }
}