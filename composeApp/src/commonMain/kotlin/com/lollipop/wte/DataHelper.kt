package com.lollipop.wte

import Platform
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.info.LResult
import com.lollipop.wte.info.json.JsonHelper
import com.lollipop.wte.info.json.JsonInfo
import com.lollipop.wte.info.json.JsonList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

class DataHelper(
    private val scope: CoroutineScope,
) {

    companion object {

        const val RES_MENU = "files/menu.wte"

        const val FILE_MENU = "menu.wte"

        const val KEY_NAME = "name"

        const val KEY_TAG = "tag"

        fun getMenuFile(): File {
            return File(Platform.fileDir, FILE_MENU)
        }

    }

    val allTagList = SnapshotStateList<String>()
    val allTagMap = SnapshotStateMap<String, String>()

    val selectTagList = SnapshotStateList<String>()
    val selectTagMap = SnapshotStateMap<String, String>()

    val dataList = SnapshotStateList<ItemInfo>()
    val dataMap = SnapshotStateMap<String, ItemInfo>()

    val filteredList = SnapshotStateList<ItemInfo>()

    val currentState = mutableStateOf(State.IDLE)

    private var isPendingLoad = false
    private var isPendingSave = false

    private val writeFlow by lazy {
        MutableSharedFlow<Int>().apply {
            collectWriteFlow(this)
        }
    }

    private fun clear() {
        selectTagList.clear()
        selectTagMap.clear()

        allTagList.clear()
        allTagMap.clear()

        dataList.clear()
        dataMap.clear()
    }

    fun putInfo(info: ItemInfo) {
        putInfo(info, true)
    }

    private fun collectWriteFlow(flow: Flow<Int>) {
        scope.launch {
            flow.collectLatest {
                delay(200)

            }
        }
    }

    private fun putInfo(info: ItemInfo, save: Boolean) {
        synchronized(this) {
            // tag 添加到集合中
            info.tagList.forEach { putTag(it) }
            val current = optItem(info.name)
            if (current != null) {
                dataMap[info.name] = info
                val index = dataList.indexOf(current)
                dataList[index] = info
            } else {
                dataMap[info.name] = info
                dataList.add(info)
            }
        }
        if (save) {
            scope.launch {
                writeFlow.emit(1)
            }
        }
    }

    private fun putTag(tag: String) {
        if (hasTag(tag)) {
            return
        }
        allTagMap[tag] = tag
        allTagList.add(tag)
    }

    private fun removeTag(tag: String) {
        if (hasTag(tag)) {
            allTagMap.remove(tag)
            allTagList.remove(tag)
        }
        unselect(tag)
    }

    private fun hasTag(tag: String): Boolean {
        return allTagMap.containsKey(tag)
    }

    private fun isSelected(tag: String): Boolean {
        return selectTagMap.containsKey(tag)
    }

    fun selected(tag: String): Boolean {
        if (isSelected(tag)) {
            return false
        }
        selectTagMap[tag] = tag
        selectTagList.add(tag)
        return true
    }

    fun unselect(tag: String) {
        selectTagMap.remove(tag)
        selectTagList.remove(tag)
    }

    private fun hasTag(info: ItemInfo, tag: String): Boolean {
        return info.has(tag)
    }

    private fun optItem(name: String): ItemInfo? {
        return dataMap[name]
    }

    fun load() {
        if (currentState.value != State.IDLE) {
            isPendingLoad = true
            return
        }
        changeState(State.LOADING)
        isPendingLoad = false
        scope.launch {
            tryDo {
                val menuFile = getMenuFile()
                if (!menuFile.exists()) {
                    JsonHelper.release(RES_MENU, menuFile)
                }
                when (val lResult = JsonHelper.readList(menuFile)) {
                    is LResult.Failure -> {
                        clear()
                    }

                    is LResult.Success -> {
                        clear()
                        val jsonList = lResult.value
                        parseMenu(jsonList, false)
                    }
                }
            }
            changeState(State.IDLE)
        }
    }

    fun addList(info: String) {
        try {
            val trimInfo = info.trim()
            val isArray = trimInfo.startsWith("[")
            val isObj = trimInfo.startsWith("{")
            if (isArray) {
                val jsonList = Platform.parseJsonList(trimInfo)
                parseMenu(jsonList, true)
            } else if (isObj) {
                val jsonInfo = Platform.parseJsonInfo(trimInfo)
                parseItem(jsonInfo, true)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun saveInfo() {
        if (currentState.value != State.IDLE) {
            isPendingSave = true
            return
        }
        changeState(State.WRITING)
        isPendingSave = false
        scope.launch {
            tryDo {
                val jsonInfo = serializeMenu(ArrayList(dataList.toList()))
                val menuFile = getMenuFile()
                JsonHelper.writeList(menuFile, jsonInfo)
            }
            changeState(State.IDLE)
        }
    }

    private fun serializeMenu(list: List<ItemInfo>): JsonList {
        val jsonList = Platform.parseJsonList("")
        list.forEach { info ->
            val jsonInfo = Platform.parseJsonInfo("")
            jsonInfo.put(KEY_NAME, info.name)
            val tagJsonList = Platform.parseJsonList("")
            info.tagList.forEach { tag ->
                tagJsonList.put(tag)
            }
            jsonInfo.put(KEY_TAG, tagJsonList)
            jsonList.put(jsonInfo)
        }
        return jsonList
    }

    private fun parseMenu(jsonList: JsonList, save: Boolean) {
        val size = jsonList.size
        for (i in 0 until size) {
            val jsonInfo = jsonList.optInfo(i) ?: continue
            parseItem(jsonInfo, save)
        }
    }

    private fun parseItem(jsonInfo: JsonInfo, save: Boolean) {
        val itemName = jsonInfo.optString(KEY_NAME, "")
        if (itemName.isNotEmpty()) {
            val itemInfo = ItemInfo(itemName)
            val tagList = jsonInfo.optArray(KEY_TAG)
            if (tagList != null) {
                parseItem(itemInfo, tagList)
            }
            putInfo(itemInfo, save)
        }
    }

    private fun parseItem(itemInfo: ItemInfo, tagList: JsonList) {
        for (j in 0 until tagList.size) {
            val tag = tagList.optString(j, "")
            if (tag.isNotEmpty()) {
                itemInfo.add(tag)
            }
        }
    }

    private suspend fun tryDo(
        errorCallback: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        try {
            block()
        } catch (e: Throwable) {
            if (errorCallback != null) {
                errorCallback(e)
            } else {
                e.printStackTrace()
            }
        }
    }

    private fun changeState(newState: State) {
        this.currentState.value = newState
        if (newState == State.IDLE) {
            if (isPendingLoad) {
                load()
            } else if (isPendingSave) {
                saveInfo()
            }
        }
    }

    enum class State {
        IDLE,
        LOADING,
        WRITING
    }

}