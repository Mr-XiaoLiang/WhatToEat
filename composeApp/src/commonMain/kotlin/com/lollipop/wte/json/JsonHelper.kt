package com.lollipop.wte.json

import Platform
import java.io.File

object JsonHelper {

    fun readInfo(file: File): JsonInfo {
        return Platform.parseJsonInfo(read(file))
    }

    fun readList(file: File): JsonList {
        return Platform.parseJsonList(read(file))
    }

    fun read(file: File): String {
        TODO("read file")
    }

}