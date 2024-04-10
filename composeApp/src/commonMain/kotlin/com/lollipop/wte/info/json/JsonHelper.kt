package com.lollipop.wte.info.json

import Platform
import com.lollipop.wte.info.LResult
import com.lollipop.wte.info.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object JsonHelper {

    suspend fun readInfo(file: File): LResult<JsonInfo> {
        return read(file).map { Platform.parseJsonInfo(it) }
    }

    suspend fun readList(file: File): LResult<JsonList> {
        return read(file).map { Platform.parseJsonList(it) }
    }

    suspend fun writeInfo(file: File, info: JsonInfo): LResult<Unit> {
        val content = LResult.tryDo { info.toString(0) }
        return when (content) {
            is LResult.Failure -> {
                LResult.Failure(content.error)
            }

            is LResult.Success -> {
                write(file, content.value)
            }
        }
    }

    suspend fun writeList(file: File, info: JsonList): LResult<Unit> {
        val content = LResult.tryDo { info.toString(0) }
        return when (content) {
            is LResult.Failure -> {
                LResult.Failure(content.error)
            }

            is LResult.Success -> {
                write(file, content.value)
            }
        }
    }

    suspend fun read(file: File): LResult<String> {
        return withContext(Dispatchers.IO) {
            readSync(file)
        }
    }

    suspend fun write(file: File, text: String): LResult<Unit> {
        return withContext(Dispatchers.IO) {
            writeSync(file, text)
        }
    }

    fun readSync(file: File): LResult<String> {
        return LResult.tryDo {
            if (!file.exists()) {
                ""
            } else {
                file.readText(Charsets.UTF_8)
            }
        }
    }

    fun writeSync(file: File, text: String): LResult<Unit> {
        return LResult.tryDo {
            file.parentFile?.mkdirs()
            file.writeText(text, Charsets.UTF_8)
        }
    }

}