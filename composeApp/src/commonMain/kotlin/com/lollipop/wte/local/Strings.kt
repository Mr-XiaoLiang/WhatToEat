package com.lollipop.wte.local

import com.lollipop.wte.local.impl.LocalStringsCn

object Strings {

    var current: LocalStrings = LocalStringsCn

    val localList: Array<LocalStrings> = arrayOf(
        LocalStringsCn
    )


}

interface LocalStrings {

    val localName: String

    val localId: String

    val selectNewTag: String

    fun hintRemoveItem(name: String): String

    val confirm: String

    val cancel: String

    val newItemLabel: String

    val itemTagLabel: String

    val newTagLabel: String

    val nameAlreadyExists: String

    val importInfoLabel: String

    val importInfoError: String

    val replace: String

    val skip: String

    val insert: String

    val copy: String

    val paste: String

}
