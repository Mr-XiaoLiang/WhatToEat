package com.lollipop.wte.preferences

import Platform
import kotlin.reflect.KProperty

object Settings {

    private val defaultPreferences: LPreferences by lazy {
        Platform.getPreferences("default")
    }

    var localLanguage by PreferencesString("")

    private class PreferencesString(private val def: String) {
        operator fun getValue(settings: Settings, property: KProperty<*>): String {
            return settings.defaultPreferences.getString(property.name, def)
        }

        operator fun setValue(settings: Settings, property: KProperty<*>, value: String) {
            settings.defaultPreferences.put(property.name, value)
        }
    }

    private class PreferencesInt(private val def: Int) {
        operator fun getValue(settings: Settings, property: KProperty<*>): Int {
            return settings.defaultPreferences.getInt(property.name, def)
        }

        operator fun setValue(settings: Settings, property: KProperty<*>, value: Int) {
            settings.defaultPreferences.put(property.name, value)
        }
    }

    private class PreferencesBoolean(private val def: Boolean) {
        operator fun getValue(settings: Settings, property: KProperty<*>): Boolean {
            return settings.defaultPreferences.getBoolean(property.name, def)
        }

        operator fun setValue(settings: Settings, property: KProperty<*>, value: Boolean) {
            settings.defaultPreferences.put(property.name, value)
        }
    }

    private class PreferencesLong(private val def: Long) {
        operator fun getValue(settings: Settings, property: KProperty<*>): Long {
            return settings.defaultPreferences.getLong(property.name, def)
        }

        operator fun setValue(settings: Settings, property: KProperty<*>, value: Long) {
            settings.defaultPreferences.put(property.name, value)
        }
    }

    private class PreferencesFloat(private val def: Float) {
        operator fun getValue(settings: Settings, property: KProperty<*>): Float {
            return settings.defaultPreferences.getFloat(property.name, def)
        }

        operator fun setValue(settings: Settings, property: KProperty<*>, value: Float) {
            settings.defaultPreferences.put(property.name, value)
        }
    }


}