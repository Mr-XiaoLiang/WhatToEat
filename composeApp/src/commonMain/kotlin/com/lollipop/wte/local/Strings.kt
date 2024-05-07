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

}
