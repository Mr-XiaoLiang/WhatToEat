package com.lollipop.wte.local.impl

import com.lollipop.wte.local.LocalStrings

object LocalStringsCn : LocalStrings {
    override val localName: String = "中文"
    override val localId: String = "cn"
    override val selectNewTag: String = "选择标签"
    override fun hintRemoveItem(name: String): String {
        return "确定删除 $name 吗？"
    }

    override val confirm: String = "确定"
    override val cancel: String = "取消"
}