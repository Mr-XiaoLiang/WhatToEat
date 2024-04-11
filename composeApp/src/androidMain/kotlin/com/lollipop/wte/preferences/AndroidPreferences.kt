package com.lollipop.wte.preferences

import android.content.SharedPreferences

class AndroidPreferences(
    private val sp: SharedPreferences
) : LPreferences {

    override fun put(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    override fun put(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    override fun put(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    override fun put(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }

    override fun put(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    override fun getString(key: String, def: String): String {
        return sp.getString(key, def) ?: def
    }

    override fun getInt(key: String, def: Int): Int {
        return sp.getInt(key, def)
    }

    override fun getBoolean(key: String, def: Boolean): Boolean {
        return sp.getBoolean(key, def)
    }

    override fun getFloat(key: String, def: Float): Float {
        return sp.getFloat(key, def)
    }

    override fun getLong(key: String, def: Long): Long {
        return sp.getLong(key, def)
    }

}