package com.lollipop.wte.info

class MenuInfo {

    private val list = ArrayList<ItemInfo>()
    private val map = HashMap<String, ItemInfo>()

    private val allTagSet = HashSet<String>()

    fun toList(): List<ItemInfo> {
        val newList = ArrayList<ItemInfo>()
        newList.addAll(list)
        return newList
    }

    fun copyTagList(): List<String> {
        val newList = ArrayList<String>()
        newList.addAll(allTagSet)
        return newList
    }

    val size: Int
        get() {
            return list.size
        }

    fun addTag(tag: String) {
        allTagSet.add(tag)
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
            // tag 添加到集合中
            allTagSet.addAll(info.tagList)
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

    fun remove(name: String): ItemInfo? {
        val current = find(name) ?: return null
        list.remove(current)
        return current
    }

}