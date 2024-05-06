package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lollipop.wte.DataHelper
import com.lollipop.wte.info.ItemInfo

@Composable
fun ManagerPanel(
    padding: PaddingValues,
    dataHelper: DataHelper
) {

    var itemAddPanel by remember { mutableStateOf(false) }
    val itemList = remember { dataHelper.dataList }
    var pendingRemoveItem by remember { mutableStateOf(ItemInfo.EMPTY) }

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8F).background(LColor.background)
    ) {
        LazyColumn {
            itemsIndexed(itemList) { index, item ->
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .background(LColor.content, RoundedCornerShape(CornerSize(6.dp)))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                pendingRemoveItem = item
                            },
                            modifier = Modifier.background(LColor.accentColor, shape = CircleShape)
                        ) {
                            Icon(Icons.Filled.Delete, null, tint = LColor.onAccentColor)
                        }
                        Text(item.name, color = LColor.onContent)
                    }
                }
            }
        }
    }

    BottomSheetDialog(
        show = pendingRemoveItem.name.isNotEmpty(),
        callClose = {
            pendingRemoveItem = ItemInfo.EMPTY
        }) {
        ItemRemovePanel(padding, dataHelper, pendingRemoveItem)
    }

    BottomSheetDialog(itemAddPanel, callClose = { itemAddPanel = false }) {
        ItemAddPanel(padding, dataHelper)
    }

}

@Composable
fun ItemRemovePanel(
    padding: PaddingValues,
    dataHelper: DataHelper,
    itemInfo: ItemInfo
) {

    // TODO
}

@Composable
fun ItemAddPanel(
    padding: PaddingValues,
    dataHelper: DataHelper,
) {
    // TODO
}