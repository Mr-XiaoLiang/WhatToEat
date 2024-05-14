package com.lollipop.wte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.lollipop.wte.DataHelper
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.local.Strings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import whattoeat.composeapp.generated.resources.Res
import whattoeat.composeapp.generated.resources.double_arrow_24dp

@Composable
fun MenuInputPanel(
    padding: PaddingValues,
    dataHelper: DataHelper
) {
    val pendingInfoList = SnapshotStateList<ItemInfo>()
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .height(maxHeight * 0.6F)
                .background(LColor.background)
        ) {
            var inputValue by mutableStateOf("")
            var errorInfo by mutableStateOf("")
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        isError = errorInfo.isEmpty(),
                        onValueChange = {
                            inputValue = it
                            errorInfo = ""
                            pendingInfoList.clear()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth().weight(1F),
                        label = {
                            Text(Strings.current.importInfoLabel)
                        }
                    )
                    Text(
                        text = errorInfo,
                        color = LColor.accentColor,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        fontSize = 10.sp
                    )
                }
                ExtendedFloatingActionButton(
                    text = {
                        Text(Strings.current.confirm)
                    },
                    onClick = {
                        dataHelper.runWith {
                            val parseResult = withContext(Dispatchers.IO) {
                                dataHelper.parseList(inputValue)
                            }
                            pendingInfoList.clear()
                            pendingInfoList.addAll(parseResult)
                            errorInfo = if (parseResult.isEmpty()) {
                                Strings.current.importInfoError
                            } else {
                                ""
                            }
                        }
                    }
                )
            }
        }
    }

    TopSheetDialog(
        show = pendingInfoList.isNotEmpty(),
        callClose = {
            // 不能手动关闭
        }
    ) { dialog ->
        if (pendingInfoList.isNotEmpty()) {
            val first = pendingInfoList.removeFirst()

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                val width = max(min(maxWidth * 0.6F, 480.dp), 260.dp)
                Card(
                    modifier = Modifier.width(width).fillMaxHeight(0.8F),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Text(
                            text = first.name,
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                .padding(24.dp, 16.dp),
                            fontSize = 16.sp
                        )

                        val oldItem = dataHelper.optItem(first.name)
                        if (oldItem == null) {
                            ImportPanel(first.tagList.toList()) { replace ->
                                if (replace) {
                                    dataHelper.putInfo(first)
                                }
                                dialog.dismiss()
                            }
                        } else {
                            ReplacePanel(
                                first.tagList.toList(),
                                oldItem.tagList.toList()
                            ) { replace ->
                                if (replace) {
                                    dataHelper.putInfo(first)
                                }
                                dialog.dismiss()
                            }
                        }

                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
private fun ReplacePanel(
    newTags: List<String>,
    oldTags: List<String>,
    resultCallback: (replace: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(1F),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FlowRow(
                modifier = Modifier.padding(
                    start = 24.dp, top = 12.dp, end = 12.dp, bottom = 12.dp
                ).background(LColor.background, RoundedCornerShape(12.dp))
                    .weight(1F).padding(6.dp)
            ) {
                oldTags.forEach {
                    Tag(
                        text = it,
                        isSelect = false
                    ) {}
                }
            }
            Icon(
                painterResource(Res.drawable.double_arrow_24dp),
                "",
                modifier = Modifier.width(48.dp).height(48.dp)
            )
            FlowRow(
                modifier = Modifier.padding(
                    start = 24.dp, top = 12.dp, end = 12.dp, bottom = 12.dp
                ).background(LColor.background, RoundedCornerShape(12.dp))
                    .weight(1F).padding(6.dp)
            ) {
                newTags.forEach {
                    Tag(
                        text = it,
                        isSelect = false
                    ) {}
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = {
                    resultCallback(false)
                },
            ) {
                Text(Strings.current.skip)
            }
            OutlinedButton(
                onClick = {
                    resultCallback(false)
                },
            ) {
                Text(Strings.current.replace)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ImportPanel(
    newTags: List<String>,
    resultCallback: (replace: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth().weight(1F).padding(
                start = 24.dp, top = 12.dp, end = 12.dp, bottom = 12.dp
            ).background(LColor.background, RoundedCornerShape(12.dp))
                .weight(1F).padding(6.dp)
        ) {
            newTags.forEach {
                Tag(
                    text = it,
                    isSelect = false
                ) {}
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = {
                    resultCallback(false)
                },
            ) {
                Text(Strings.current.skip)
            }
            OutlinedButton(
                onClick = {
                    resultCallback(false)
                },
            ) {
                Text(Strings.current.replace)
            }
        }
    }
}
