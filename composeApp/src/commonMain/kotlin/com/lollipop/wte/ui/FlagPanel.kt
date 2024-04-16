package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FlagPanel(padding: PaddingValues, dataList: SnapshotStateList<String>, miniMode: Boolean) {
    if (miniMode) {
        FlagPanelByPhone(padding, dataList)
    } else {
        FlagPanelByTablet(padding, dataList)
    }
}

@Composable
private fun FlagPanelByPhone(padding: PaddingValues, dataList: SnapshotStateList<String>) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val itemHeight = 60.dp
        var spanCount = (maxHeight / itemHeight).toInt()
        if (spanCount < 1) {
            spanCount = 1
        }
        LazyHorizontalStaggeredGrid(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            rows = StaggeredGridCells.Fixed(spanCount),
            contentPadding = padding.wrapperOf(
                topEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                startEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                endEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                bottomEdge = PaddingValuesWrapper.Edge.Max(8.dp)
            ),
            horizontalItemSpacing = 8.dp,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(dataList, key = { _, p -> p }) { i, infp ->
                // TODO 创建item
                Text(
                    "FlagPanelByPhone",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red)
                )
            }
        }
    }

}

@Composable
private fun FlagPanelByTablet(padding: PaddingValues, dataList: SnapshotStateList<String>) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val itemHeight = 60.dp
        var spanCount = (maxHeight / itemHeight).toInt()
        if (spanCount < 1) {
            spanCount = 1
        }
        LazyHorizontalStaggeredGrid(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            rows = StaggeredGridCells.Fixed(spanCount),
            contentPadding = padding.wrapperOf(
                topEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                startEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                endEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                bottomEdge = PaddingValuesWrapper.Edge.Max(8.dp)
            ),
            horizontalItemSpacing = 8.dp,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(dataList, key = { _, p -> p }) { i, infp ->
                // TODO 创建item
                Text(
                    "FlagPanelByPhone",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red)
                )
            }
        }
    }
    Text(
        "FlagPanelByTablet",
        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red)
    )
}
