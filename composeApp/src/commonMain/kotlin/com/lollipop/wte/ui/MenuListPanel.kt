package com.lollipop.wte.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lollipop.wte.info.ItemInfo

@Composable
fun MenuListPanel(padding: PaddingValues, dataList: SnapshotStateList<ItemInfo>) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val itemWidth = 240.dp

        var spanCount = (maxWidth / itemWidth).toInt()
        if (spanCount < 1) {
            spanCount = 1
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            columns = StaggeredGridCells.Fixed(spanCount),
            contentPadding = padding.wrapperOf(
                topEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                startEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                endEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                bottomEdge = PaddingValuesWrapper.Edge.Max(8.dp)
            ),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(dataList, key = { _, p -> p.name }) { i, info ->
                // TODO 创建item
            }
        }
    }

}
