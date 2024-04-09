import com.lollipop.wte.json.JsonInfo
import com.lollipop.wte.json.JsonList
import java.io.File

interface Platform {
    val name: String

    val fileDir: File

}

expect fun getPlatform(): Platform

expect fun parseJsonInfo(info: String): JsonInfo

expect fun parseJsonList(info: String): JsonList