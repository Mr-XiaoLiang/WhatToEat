package com.lollipop.navigator2

import kotlin.reflect.KProperty

fun interface NavIntent {

    fun intent(): NavIntentInfo
}

open class NavIntentInfo {

    var data: MutableMap<String, Any> = hashMapOf()
        private set

    fun update(input: MutableMap<String, Any>) {
        data = input
    }

    protected fun self(def: Int) = IntDelegate(def)
    protected fun self(def: String) = StringDelegate(def)
    protected fun self(def: Long) = LongDelegate(def)
    protected fun self(def: Float) = FloatDelegate(def)
    protected fun self(def: Double) = DoubleDelegate(def)
    protected fun self(def: Boolean) = BooleanDelegate(def)
    protected fun <T : Any> self(def: () -> T) = ObjectDelegate(def)

    protected class IntDelegate(private val def: Int = 0) {

        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): Int {
            val any = intentInfo.data[property.name] ?: return def
            if (any is Int) {
                return any
            }
            return def
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: Int) {
            intentInfo.data[property.name] = i
        }
    }

    protected class LongDelegate(private val def: Long = 0L) {

        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): Long {
            val any = intentInfo.data[property.name] ?: return def
            if (any is Long) {
                return any
            }
            return def
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: Long) {
            intentInfo.data[property.name] = i
        }
    }

    protected class BooleanDelegate(private val def: Boolean = false) {

        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): Boolean {
            val any = intentInfo.data[property.name] ?: return def
            if (any is Boolean) {
                return any
            }
            return def
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: Boolean) {
            intentInfo.data[property.name] = i
        }
    }

    protected class FloatDelegate(private val def: Float = 0F) {

        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): Float {
            val any = intentInfo.data[property.name] ?: return def
            if (any is Float) {
                return any
            }
            return def
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: Float) {
            intentInfo.data[property.name] = i
        }
    }

    protected class DoubleDelegate(private val def: Double = 0.0) {

        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): Double {
            val any = intentInfo.data[property.name] ?: return def
            if (any is Double) {
                return any
            }
            return def
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: Double) {
            intentInfo.data[property.name] = i
        }
    }

    protected class StringDelegate(private val def: String = "") {

        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): String {
            val any = intentInfo.data[property.name] ?: return def
            if (any is String) {
                return any
            }
            return def
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: String) {
            intentInfo.data[property.name] = i
        }
    }

    protected class ObjectDelegate<T : Any>(private val def: (() -> T)) {

        @Suppress("UNCHECKED_CAST")
        operator fun getValue(intentInfo: NavIntentInfo, property: KProperty<*>): T {
            val any = intentInfo.data[property.name] ?: return def()
            try {
                return any as T
            } catch (_: Throwable) {
            }
            return def()
        }

        operator fun setValue(intentInfo: NavIntentInfo, property: KProperty<*>, i: T) {
            intentInfo.data[property.name] = i
        }
    }

}

inline fun <reified T : NavIntentInfo> T.sync(data: NavIntentInfo): T {
    val intent = this
    intent.update(data.data)
    return intent
}
