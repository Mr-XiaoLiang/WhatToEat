package com.lollipop.wte.info

class ItemInfo(val name: String) {

    companion object {
        val EMPTY = ItemInfo("")
    }

    val tagList = HashSet<String>()

    fun has(tag: String): Boolean {
        return tagList.contains(tag)
    }

    fun add(tag: String) {
        tagList.add(tag)
    }

    fun remove(tag: String) {
        tagList.remove(tag)
    }

}