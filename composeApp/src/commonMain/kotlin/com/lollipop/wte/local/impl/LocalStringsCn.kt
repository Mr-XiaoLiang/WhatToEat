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
    override val newItemLabel: String = "名称"
    override val itemTagLabel: String = "标签"
    override val newTagLabel: String = "新增标签"
    override val nameAlreadyExists: String = "名称已经存在"
    override val importInfoLabel: String = "请输入 JSON"
    override val importInfoError: String = "解析结果为空"
    override val replace: String = "替换"
    override val skip: String = "跳过"
    override val insert: String = "添加"
    override val copy: String = "复制"
    override val paste: String = "粘贴"
}