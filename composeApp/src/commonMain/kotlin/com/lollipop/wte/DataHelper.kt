package com.lollipop.wte

import Platform
import com.lollipop.wte.info.ItemInfo
import com.lollipop.wte.info.LResult
import com.lollipop.wte.info.MenuInfo
import com.lollipop.wte.info.json.JsonHelper
import com.lollipop.wte.info.json.JsonList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class DataHelper(
    private val scope: CoroutineScope,
    private val stateCallback: StateCallback
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

    private val menuInfo = MenuInfo()

    var currentState = State.IDLE
        private set

    fun fetchMenuList(): List<ItemInfo> {
        return menuInfo.toList()
    }

    fun fetchTagList(): List<String> {
        return menuInfo.copyTagList()
    }

    private var pendingLoad: LoadCallback? = null

    private var pendingWrite: WriteCallback? = null

    fun load(callback: LoadCallback) {
        pendingLoad = callback
        if (currentState != State.IDLE) {
            return
        }
        changeState(State.LOADING)
        tryLaunch {
            val menuFile = getMenuFile()
            if (!menuFile.exists()) {
                JsonHelper.release(RES_MENU, menuFile)
            }
            when (val lResult = JsonHelper.readList(menuFile)) {
                is LResult.Failure -> {
                    menuInfo.clear()
                }

                is LResult.Success -> {
                    menuInfo.clear()
                    val jsonList = lResult.value
                    parseMenu(jsonList)
                }
            }
            onLoadEnd()
            changeState(State.IDLE)
        }
    }

    private fun parseMenu(jsonList: JsonList) {
        val size = jsonList.size
        for (i in 0 until size) {
            val jsonInfo = jsonList.optInfo(i) ?: continue
            val itemName = jsonInfo.optString(KEY_NAME, "")
            if (itemName.isNotEmpty()) {
                val itemInfo = ItemInfo(itemName)
                val tagList = jsonInfo.optArray(KEY_TAG)
                if (tagList != null) {
                    parseItem(itemInfo, tagList)
                }
                menuInfo.put(itemInfo)
            }
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

    private fun onLoadEnd() {
        pendingLoad?.onLoaded()
        pendingLoad = null
    }

    private fun tryLaunch(
        errorCallback: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        scope.launch {
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
    }

    private fun changeState(newState: State) {
        this.currentState = newState
        this.stateCallback.onStateChanged(newState)
    }

    fun interface LoadCallback {
        fun onLoaded()
    }

    fun interface WriteCallback {
        fun onWritten()
    }

    fun interface StateCallback {
        fun onStateChanged(state: State)
    }

    enum class State {
        IDLE,
        LOADING,
        WRITING
    }

}