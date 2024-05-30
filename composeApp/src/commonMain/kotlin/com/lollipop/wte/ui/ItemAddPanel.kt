package com.lollipop.wte.ui

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lollipop.navigator2.BackDispatcher
import com.lollipop.navigator2.NavIntent
import com.lollipop.navigator2.Navigator2
import com.lollipop.navigator2.sync
import com.lollipop.wte.DataHelper
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.local.Strings
import com.lollipop.wte.router.Router


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemAddPanel(
    padding: PaddingValues, navigator2: Navigator2, navIntent: NavIntent, back: BackDispatcher
) {
    val dataHelper = DataHelper
    val intent = Router.ItemAdd.Intent()
    intent.sync(navIntent.intent())
    var name by remember { mutableStateOf(intent.nameValue) }
    val selectedMap = remember { SnapshotStateMap<String, String>() }
    if (name.isNotEmpty() && selectedMap.isEmpty()) {
        dataHelper.optItem(name)?.let { item ->
            item.tagList.forEach { tag ->
                selectedMap[tag] = tag
            }
        }
    }
    var tagValue by mutableStateOf("")
    val allTagList = remember { dataHelper.allTagList }
    var inputError by mutableStateOf(false)
    ActionBarGroup(
        onBack = back,
        actionButtons = {}
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
                    Tag(
                        text = info,
                        isSelect = isSelect,
                    ) {
                        if (isSelect) {
                            selectedMap.remove(info)
                        } else {
                            selectedMap[info] = info
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
                        intent.nameValue = ""
                        selectedMap.clear()
                        back()
//                        Router.Main.go(navigator2)
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}