package com.lollipop.wte.json

interface JsonInfo {

    fun toString(tabs: Int): String

    fun put(key: String, value: String)

    fun put(key: String, value: Int)

    fun put(key: String, value: Boolean)

    fun put(key: String, value: Double)

    fun put(key: String, value: JsonInfo)

    fun put(key: String, value: JsonList)

    fun optString(key: String, def: String): String

    fun optInt(key: String, def: Int): Int

    fun optBoolean(key: String, def: Boolean): Boolean

    fun optDouble(key: String, def: Double): Double

    fun optInfo(key: String): JsonInfo?

    fun optArray(key: String): JsonList?

}

interface JsonList {

    val size: Int

    fun toString(tabs: Int): String

    fun put(value: JsonInfo)

    fun put(value: JsonList)

    fun put(value: String)

    fun put(value: Int)

    fun put(value: Boolean)

    fun put(value: Double)

    fun optString(index: Int, def: String): String

    fun optInt(index: Int, def: Int): Int

    fun optBoolean(index: Int, def: Boolean): Boolean

    fun optDouble(index: Int, def: Double): Double

    fun optInfo(index: Int): JsonInfo?

    fun optArray(index: Int): JsonList?

}
