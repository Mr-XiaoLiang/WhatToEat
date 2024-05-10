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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.lollipop.wte.DataHelper
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.local.Strings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import whattoeat.composeapp.generated.resources.Res
import whattoeat.composeapp.generated.resources.download_24dp
import whattoeat.composeapp.generated.resources.upload_24dp

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
fun ManagerPanel(
    padding: PaddingValues,
    dataHelper: DataHelper
) {

    val nameValue = remember { mutableStateOf("") }
    val selectedMap = remember { SnapshotStateMap<String, String>() }

    var itemAddPanel by remember { mutableStateOf(false) }
    val itemList = remember { dataHelper.dataList }
    var pendingRemoveItem by remember { mutableStateOf(ItemInfo.EMPTY) }

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8F).background(LColor.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {
                    // TODO
                }
            ) {
                Icon(painterResource(Res.drawable.download_24dp), "")
            }
            IconButton(
                onClick = {
                    // TODO
                }
            ) {
                Icon(painterResource(Res.drawable.upload_24dp), "")
            }
            IconButton(
                onClick = {
                    itemAddPanel = true
                }
            ) {
                Icon(Icons.Filled.Add, "")
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
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
                        Spacer(modifier = Modifier.height(8.dp))
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(0.9F).height(1.dp)
                                .background(LColor.background)
                        )
                    }
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
        ItemAddPanel(nameValue, selectedMap, padding, dataHelper, it)
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemAddPanel(
    nameValue: MutableState<String>,
    selectedMap: SnapshotStateMap<String, String>,
    padding: PaddingValues,
    dataHelper: DataHelper,
    dialog: DialogInterface
) {
    var name by nameValue
    var tagValue by mutableStateOf("")
    val allTagList = remember { dataHelper.allTagList }
    var inputError by mutableStateOf(false)
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val width = max(min(maxWidth * 0.6F, 480.dp), 260.dp)
        Card(
            modifier = Modifier.width(width).fillMaxHeight(0.8F),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name,
                    isError = inputError,
                    onValueChange = {
                        name = it
                        inputError = dataHelper.dataMap.containsKey(it)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                    label = {
                        Text(Strings.current.newItemLabel)
                    }
                )
                if (inputError) {
                    Text(
                        text = Strings.current.nameAlreadyExists,
                        color = LColor.accentColor,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        fontSize = 10.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = tagValue,
                        onValueChange = {
                            tagValue = it
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).weight(1F),
                        label = {
                            Text(Strings.current.newTagLabel)
                        }
                    )
                    IconButton(
                        onClick = {
                            dataHelper.putTag(tagValue)
                            tagValue = ""
                        }
                    ) {
                        Icon(Icons.Filled.Done, "")
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                }
                FlowRow(
                    modifier = Modifier.weight(1F).fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    allTagList.forEach { info ->
                        val isSelect = selectedMap.containsKey(info)
                        Box(
                            modifier = Modifier.wrapContentWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .combinedClickable {
                                        if (isSelect) {
                                            selectedMap.remove(info)
                                        } else {
                                            selectedMap[info] = info
                                        }
                                    },
                            ) {
                                Text(
                                    modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                        .background(getItemBackgroundColor(isSelect))
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    text = info,
                                    textAlign = TextAlign.Center,
                                    color = getItemContentColor(isSelect),
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(text = Strings.current.confirm)
                        },
                        onClick = {
                            dataHelper.putInfo(
                                ItemInfo(name).apply {
                                    tagList.addAll(selectedMap.keys)
                                }
                            )
                            dialog.dismiss()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun getItemBackgroundColor(selected: Boolean): Color {
    return if (selected) {
        LColor.themeColor
    } else {
        LColor.background
    }
}

private fun getItemContentColor(selected: Boolean): Color {
    return if (selected) {
        LColor.onThemeColor
    } else {
        LColor.onBackground
    }
}