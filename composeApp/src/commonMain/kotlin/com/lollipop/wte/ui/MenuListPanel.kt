package com.lollipop.wte.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lollipop.wte.DataHelper

@Composable
fun MenuListPanel(padding: PaddingValues, dataHelper: DataHelper) {
    val dataList = remember { dataHelper.filteredList }
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            backgroundColor = Color.White
        ) {
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
                    itemsIndexed(dataList) { i, info ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                text = info.name,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

}
