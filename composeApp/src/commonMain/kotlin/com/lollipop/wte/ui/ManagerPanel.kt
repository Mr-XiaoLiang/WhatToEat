package com.lollipop.wte.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.lollipop.wte.DataHelper
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.local.Strings

@OptIn(ExperimentalLayoutApi::class)
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
            items(itemList) { item ->
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
                            modifier = Modifier.width(24.dp).height(24.dp).padding(4.dp)
                        ) {
                            Icon(Icons.Filled.Delete, null, tint = LColor.accentColor)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(item.name, color = LColor.onContent)
                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    ) {
                        item.tagList.forEach {
                            Text(
                                text = it,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    .background(
                                        LColor.background,
                                        RoundedCornerShape(6.dp)
                                    ).padding(horizontal = 6.dp, vertical = 2.dp),
                                color = LColor.onBackground
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(0.9F).height(1.dp)
                            .background(LColor.background)
                    )
                }
            }
        }
    }

    TopSheetDialog(
        show = pendingRemoveItem.name.isNotEmpty(),
        callClose = {
            pendingRemoveItem = ItemInfo.EMPTY
        }) {
        ItemRemovePanel(padding, dataHelper, pendingRemoveItem, it)
    }

    TopSheetDialog(itemAddPanel, callClose = { itemAddPanel = false }) {
        ItemAddPanel(padding, dataHelper)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemRemovePanel(
    padding: PaddingValues,
    dataHelper: DataHelper,
    itemInfo: ItemInfo,
    dialog: DialogInterface
) {
    BoxWithConstraints {
        val width = max(min(maxWidth * 0.6F, 480.dp), 260.dp)
        Card(
            modifier = Modifier.width(width).wrapContentHeight().background(LColor.background)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Strings.current.hintRemoveItem(itemInfo.name),
                    modifier = Modifier.padding(32.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = Strings.current.cancel,
                        color = LColor.onThemeColor,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(LColor.themeColor, RoundedCornerShape(8.dp))
                            .combinedClickable {
                                dialog.dismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)

                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = Strings.current.confirm,
                        color = LColor.onAccentColor,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(LColor.accentColor, RoundedCornerShape(8.dp))
                            .combinedClickable {
                                dataHelper.removeInfo(itemInfo)
                                dialog.dismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)

                    )
                }
            }

        }
    }
}

@Composable
fun ItemAddPanel(
    padding: PaddingValues,
    dataHelper: DataHelper,
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.8F).fillMaxHeight(0.8F).background(LColor.background)
    ) {

    }
    // TODO
}