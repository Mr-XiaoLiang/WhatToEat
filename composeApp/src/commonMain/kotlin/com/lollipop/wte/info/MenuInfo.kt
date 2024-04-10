package com.lollipop.wte.info

class MenuInfo {

    private val list = ArrayList<ItemInfo>()
    private val map = HashMap<String, ItemInfo>()

    fun toList(): List<ItemInfo> {
        val newList = ArrayList<ItemInfo>()
        newList.addAll(list)
        return newList
    }

    val size: Int
        get() {
            return list.size
        }

    fun find(name: String): ItemInfo? {
        return map[name]
    }

    fun clear() {
        synchronized(list) {
            list.clear()
            map.clear()
        }
    }

    fun put(info: ItemInfo): ItemInfo {
        synchronized(list) {
            val current = map[info.name]
            if (current != null) {
                current.tagList.addAll(info.tagList)
                return current
            }
            map[info.name] = info
            list.add(info)
            return info
        }
    }

    fun put(name: String): ItemInfo {
        synchronized(list) {
            val current = find(name)
            if (current != null) {
                return current
            }
            val newInfo = ItemInfo(name)
            map[name] = newInfo
            list.add(newInfo)
            return newInfo
        }
    }

    fun remove(name: String): ItemInfo? {
        val current = find(name) ?: return null
        list.remove(current)
        return current
    }

}