import com.lollipop.wte.json.JsonInfo
import com.lollipop.wte.json.JsonList
import java.io.File

expect object Platform {
    val name: String

    val fileDir: File

    fun parseJsonInfo(info: String): JsonInfo

    fun parseJsonList(info: String): JsonList

}

