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
import loggerOf
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

    private val log = loggerOf()

    val allTagList = SnapshotStateList<String>()
    val allTagMap = SnapshotStateMap<String, String>()

    val selectTagList = SnapshotStateList<String>()
    val selectTagMap = SnapshotStateMap<String, String>()

    val dataList = SnapshotStateList<ItemInfo>()
    val dataMap = SnapshotStateMap<String, ItemInfo>()

    val filteredList = SnapshotStateList<ItemInfo>()
    val filteredMap = SnapshotStateMap<String, ItemInfo>()

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

        filteredList.clear()
        filteredMap.clear()

        allTagList.clear()
        allTagMap.clear()

        dataList.clear()
        dataMap.clear()
    }

    fun putInfo(info: ItemInfo) {
        putInfo(info, true)
    }

    private fun selectItem(item: ItemInfo) {
        item.tagList.forEach { t ->
            if (isSelected(t)) {
                if (!filteredMap.containsKey(item.name)) {
                    filteredMap[item.name] = item
                    filteredList.add(item)
                }
                return
            }
        }
    }

    private fun unselectItem(item: ItemInfo, removeTag: String) {
        // 前提是包含，这次我们把它移除
        if (item.tagList.contains(removeTag)) {
            // 然后检查是否其他tag是选中的，如果是，那么还应该继续留着
            item.tagList.forEach { t ->
                if (isSelected(t)) {
                    return
                }
            }
            // 否则，我们把它移除
            filteredMap.remove(item.name)
            filteredList.remove(item)
        }
    }

    private fun collectWriteFlow(flow: Flow<Int>) {
        scope.launch {
            flow.collectLatest {
                delay(200)
                saveInfo()
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

    fun trigger(tag: String) {
        log("trigger $tag")
        if (isSelected(tag)) {
            unselect(tag)
        } else {
            selected(tag)
        }
    }

    private fun isSelected(tag: String): Boolean {
        return selectTagMap.containsKey(tag)
    }

    fun selected(tag: String): Boolean {
        log("selected $tag")
        if (isSelected(tag)) {
            log("selected isSelected = true")
            return false
        }
        log("selected before size = ${filteredList.size}")
        if (selectTagMap.isEmpty()) {
            filteredMap.clear()
            filteredList.clear()
        }
        selectTagMap[tag] = tag
        selectTagList.add(tag)
        dataList.forEach { item ->
            selectItem(item)
        }
        log("selected after size = ${filteredList.size}")
        return true
    }

    fun unselect(tag: String) {
        log("unselect $tag")
        if (!isSelected(tag)) {
            log("unselect isSelected = false")
            return
        }
        selectTagMap.remove(tag)
        selectTagList.remove(tag)
        if (passAllBySelectEmpty()) {
            log("unselect passAllBySelectEmpty")
            return
        }
        log("unselect before size = ${filteredList.size}")
        dataList.forEach { item ->
            unselectItem(item, tag)
        }
        log("unselect after size = ${filteredList.size}")
    }

    private fun updateSelect() {
        if (passAllBySelectEmpty()) {
            return
        }
        if (selectTagMap.isEmpty()) {
            // 空了就显示全部
            filteredMap.clear()
            filteredList.clear()
            filteredMap.putAll(dataMap)
            filteredList.addAll(dataList)
            return
        }
        filteredMap.clear()
        filteredList.clear()
        dataList.forEach { item ->
            selectItem(item)
        }
    }

    private fun passAllBySelectEmpty(): Boolean {
        if (selectTagMap.isEmpty()) {
            // 空了就显示全部
            filteredMap.clear()
            filteredList.clear()
            filteredMap.putAll(dataMap)
            filteredList.addAll(dataList)
            return true
        }
        return false
    }

    private fun hasTag(info: ItemInfo, tag: String): Boolean {
        return info.has(tag)
    }

    private fun optItem(name: String): ItemInfo? {
        return dataMap[name]
    }

    fun load() {
//        log("load filteredList is ${System.identityHashCode(filteredList)}")
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
                // 加载之后刷新一次
                updateSelect()
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