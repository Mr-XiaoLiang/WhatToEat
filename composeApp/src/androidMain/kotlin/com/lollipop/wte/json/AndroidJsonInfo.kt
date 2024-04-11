package com.lollipop.wte.json

import com.lollipop.wte.info.json.JsonInfo
import com.lollipop.wte.info.json.JsonList
import org.json.JSONArray
import org.json.JSONObject

object AndroidJson {
    fun parseInfo(info: String): AndroidJsonInfo {
        if (info.isEmpty()) {
            return AndroidJsonInfo(JSONObject())
        }
        return try {
            AndroidJsonInfo(JSONObject(info))
        } catch (e: Throwable) {
            e.printStackTrace()
            AndroidJsonInfo(JSONObject())
        }
    }

    fun parseInfo(info: JsonInfo): AndroidJsonInfo {
        if (info is AndroidJsonInfo) {
            return info
        }
        return parseInfo(info.toString(0))
    }

    fun parseList(info: String): AndroidJsonList {
        if (info.isEmpty()) {
            return AndroidJsonList(JSONArray())
        }
        return try {
            AndroidJsonList(JSONArray(info))
        } catch (e: Throwable) {
            e.printStackTrace()
            AndroidJsonList(JSONArray())
        }
    }

    fun parseList(info: JsonList): AndroidJsonList {
        if (info is AndroidJsonList) {
            return info
        }
        return parseList(info.toString(0))
    }
}

class AndroidJsonInfo(val json: JSONObject) : JsonInfo {

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
        json.put(key, AndroidJson.parseInfo(value).json)
    }

    override fun put(key: String, value: JsonList) {
        json.put(key, AndroidJson.parseList(value).json)
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
        return AndroidJsonInfo(info)
    }

    override fun optArray(key: String): JsonList? {
        val info = json.optJSONArray(key) ?: return null
        return AndroidJsonList(info)
    }
}

class AndroidJsonList(val json: JSONArray) : JsonList {
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
        json.put(AndroidJson.parseInfo(value).json)
    }

    override fun put(value: JsonList) {
        json.put(AndroidJson.parseList(value).json)
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
        return AndroidJsonInfo(info)
    }

    override fun optArray(index: Int): JsonList? {
        val info = json.optJSONArray(index) ?: return null
        return AndroidJsonList(info)
    }

}
