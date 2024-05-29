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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.lollipop.navigator2.BackDispatcher
import com.lollipop.navigator2.Navigator2
import com.lollipop.wte.DataHelper
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.local.Strings
import com.lollipop.wte.router.Router
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import whattoeat.composeapp.generated.resources.Res
import whattoeat.composeapp.generated.resources.download_24dp
import whattoeat.composeapp.generated.resources.upload_24dp

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ManagerPanel(padding: PaddingValues, navigator2: Navigator2, back: BackDispatcher) {
    val dataHelper = DataHelper
    val itemList = remember { dataHelper.dataList }
    var pendingRemoveItem by remember { mutableStateOf(ItemInfo.EMPTY) }

    ActionBarGroup(
        isShowBack = true,
        onBack = back,
        actionButtons = {
            IconButton(
                onClick = {
                    Router.MenuInput.go(navigator2)
                }
            ) {
                Icon(painterResource(Res.drawable.download_24dp), "")
            }
            IconButton(
                onClick = {
                    Router.MenuOutput.go(navigator2)
                }
            ) {
                Icon(painterResource(Res.drawable.upload_24dp), "")
            }
            IconButton(
                onClick = {
                    Router.ItemAdd.go(navigator2) {
                        nameValue = ""
                    }
                }
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(itemList) { item ->
                ItemCard(item, navigator2) {
                    pendingRemoveItem = item
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

}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemCard(item: ItemInfo, navigator2: Navigator2, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .combinedClickable {
                Router.ItemAdd.go(navigator2) {
                    nameValue = item.name
                }
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F).wrapContentHeight()) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = item.name, color = LColor.onContent
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            ) {
                item.tagList.forEach {
                    Tag(
                        text = it,
                        isSelect = false
                    ) {}
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(1.dp)
                    .background(LColor.background)
            )
        }
        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier.width(48.dp).height(48.dp).padding(12.dp)
        ) {
            Icon(Icons.Filled.Delete, null, tint = LColor.accentColor)
        }
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
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
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
