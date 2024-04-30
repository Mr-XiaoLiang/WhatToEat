package com.lollipop.wte.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lollipop.wte.DataHelper
import com.lollipop.wte.local.Strings

@Composable
fun FlagPanel(
    padding: PaddingValues,
    miniMode: Boolean,
    dataHelper: DataHelper,
    drawerCallback: () -> Unit
) {
    val tagList = remember { dataHelper.allTagList }
    val selectedList = remember { dataHelper.selectTagList }
    val selectedMap = remember { dataHelper.selectTagMap }
    if (miniMode) {
        FlagPanelByPhone(padding, selectedList, drawerCallback)
    } else {
        FlagPanelByTablet(padding, tagList, selectedMap, dataHelper::trigger)
    }
}

@Composable
private fun FlagPanelByPhone(
    padding: PaddingValues,
    dataList: SnapshotStateList<String>,
    drawerCallback: () -> Unit
) {

    if (dataList.isEmpty()) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = Strings.current.selectNewTag)
                },
                onClick = drawerCallback
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                rows = StaggeredGridCells.Adaptive(40.dp),
                contentPadding = padding.wrapperOf(
                    topEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                    startEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                    endEdge = PaddingValuesWrapper.Edge.Max(8.dp),
                    bottomEdge = PaddingValuesWrapper.Edge.Max(8.dp)
                ),
                horizontalItemSpacing = 8.dp,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(dataList, key = { _, p -> p }) { i, info ->
                    Card(
                        modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                    ) {
                        Text(
                            modifier = Modifier.fillMaxHeight().wrapContentWidth()
                                .background(getItemColor(true))
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            textAlign = TextAlign.Center,
                            text = info
                        )
                    }
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
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .background(getItemColor(isSelect))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        text = info,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private val colorSelect = Color(0xFF3333AA)
private val colorDefault = Color.White

private fun getItemColor(selected: Boolean): Color {
    return if (selected) {
        colorSelect
    } else {
        colorDefault
    }
}
