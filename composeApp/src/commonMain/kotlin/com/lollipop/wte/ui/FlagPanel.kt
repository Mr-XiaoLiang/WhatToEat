package com.lollipop.wte.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lollipop.wte.DataHelper

@Composable
fun FlagPanel(
    padding: PaddingValues,
    miniMode: Boolean,
    dataHelper: DataHelper,
) {
    val tagList = remember { dataHelper.allTagList }
    val selectedList = remember { dataHelper.selectTagList }
    val selectedMap = remember { dataHelper.selectTagMap }
    if (miniMode) {
        FlagPanelByPhone(padding, selectedList)
    } else {
        FlagPanelByTablet(padding, tagList, selectedMap, dataHelper::trigger)
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
            itemsIndexed(dataList, key = { _, p -> p }) { i, info ->
                Card(
                    modifier = Modifier.fillMaxHeight().wrapContentWidth()
                        .background(Color.Red, shape = RoundedCornerShape(6.dp)),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        text = info
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FlagPanelByTablet(
    padding: PaddingValues,
    tagList: SnapshotStateList<String>,
    selectedMap: SnapshotStateMap<String, String>,
    onTagClick: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val itemHeight = 60.dp
        var spanCount = (maxHeight / itemHeight).toInt()
        if (spanCount < 1) {
            spanCount = 1
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            contentPadding = padding.wrapperOf(
                topEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                startEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                endEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                bottomEdge = PaddingValuesWrapper.Edge.Max(8.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(tagList, key = { _, p -> p }) { i, info ->
                val isSelect = selectedMap.containsKey(info)
                Card(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .combinedClickable {
                            onTagClick(info)
                        },
                ) {
                    val sf = if (isSelect) {
                        " *"
                    } else {
                        ""
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        text = info + sf,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
