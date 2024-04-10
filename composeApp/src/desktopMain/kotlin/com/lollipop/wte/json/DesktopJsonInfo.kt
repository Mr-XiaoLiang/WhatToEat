package com.lollipop.wte.json

import com.lollipop.wte.info.json.JsonInfo
import com.lollipop.wte.info.json.JsonList
import org.json.JSONArray
import org.json.JSONObject

object DesktopJson {
    fun parseInfo(info: String): DesktopJsonInfo {
        if (info.isEmpty()) {
            return DesktopJsonInfo(JSONObject())
        }
        return try {
            DesktopJsonInfo(JSONObject(info))
        } catch (e: Throwable) {
            e.printStackTrace()
            DesktopJsonInfo(JSONObject())
        }
    }

    fun parseInfo(info: JsonInfo): DesktopJsonInfo {
        if (info is DesktopJsonInfo) {
            return info
        }
        return parseInfo(info.toString(0))
    }

    fun parseList(info: String): DesktopJsonList {
        if (info.isEmpty()) {
            return DesktopJsonList(JSONArray())
        }
        return try {
            DesktopJsonList(JSONArray(info))
        } catch (e: Throwable) {
            e.printStackTrace()
            DesktopJsonList(JSONArray())
        }
    }

    fun parseList(info: JsonList): DesktopJsonList {
        if (info is DesktopJsonList) {
            return info
        }
        return parseList(info.toString(0))
    }
}

class DesktopJsonInfo(val json: JSONObject) : JsonInfo {

    override fun toString(tabs: Int): String {
        if (tabs < 1) {
            return json.toString()
        }
        return json.toString(tabs)
    }

    override fun put(key: String, value: String) {
        json.put(key, value)
    }

    override fun put(key: String, value: Int) {
        json.put(key, value)
    }

    override fun put(key: String, value: Boolean) {
        json.put(key, value)
    }

    override fun put(key: String, value: Double) {
        json.put(key, value)
    }

    override fun put(key: String, value: JsonInfo) {
        json.put(key, DesktopJson.parseInfo(value).json)
    }

    override fun put(key: String, value: JsonList) {
        json.put(key, DesktopJson.parseList(value).json)
    }

    override fun optString(key: String, def: String): String {
        return json.optString(key, def) ?: def
    }

    override fun optInt(key: String, def: Int): Int {
        return json.optInt(key, def) ?: def
    }

    override fun optBoolean(key: String, def: Boolean): Boolean {
        return json.optBoolean(key, def) ?: def
    }

    override fun optDouble(key: String, def: Double): Double {
        return json.optDouble(key, def) ?: def
    }

    override fun optInfo(key: String): JsonInfo? {
        val info = json.optJSONObject(key) ?: return null
        return DesktopJsonInfo(info)
    }

    override fun optArray(key: String): JsonList? {
        val info = json.optJSONArray(key) ?: return null
        return DesktopJsonList(info)
    }
}

class DesktopJsonList(val json: JSONArray) : JsonList {
    override val size: Int
        get() {
            return json.length()
        }

    override fun toString(tabs: Int): String {
        if (tabs < 1) {
            return json.toString()
        }
        return json.toString(tabs)
    }

    override fun put(value: JsonInfo) {
        json.put(DesktopJson.parseInfo(value).json)
    }

    override fun put(value: JsonList) {
        json.put(DesktopJson.parseList(value).json)
    }

    override fun put(value: String) {
        json.put(value)
    }

    override fun put(value: Int) {
        json.put(value)
    }

    override fun put(value: Boolean) {
        json.put(value)
    }

    override fun put(value: Double) {
        json.put(value)
    }

    override fun optString(index: Int, def: String): String {
        return json.optString(index, def)
    }

    override fun optInt(index: Int, def: Int): Int {
        return json.optInt(index, def)
    }

    override fun optBoolean(index: Int, def: Boolean): Boolean {
        return json.optBoolean(index, def)
    }

    override fun optDouble(index: Int, def: Double): Double {
        return json.optDouble(index, def)
    }

    override fun optInfo(index: Int): JsonInfo? {
        val info = json.optJSONObject(index) ?: return null
        return DesktopJsonInfo(info)
    }

    override fun optArray(index: Int): JsonList? {
        val info = json.optJSONArray(index) ?: return null
        return DesktopJsonList(info)
    }

}
