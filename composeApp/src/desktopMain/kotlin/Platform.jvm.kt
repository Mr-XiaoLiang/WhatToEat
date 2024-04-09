import com.lollipop.wte.json.DesktopJson
import com.lollipop.wte.json.JsonInfo
import com.lollipop.wte.json.JsonList
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val fileDir: File by lazy {
        File(System.getProperty("user.home"), Config.APP_NAME).apply {
            mkdirs()
        }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun parseJsonInfo(info: String): JsonInfo {
    return DesktopJson.parseInfo(info)
}

actual fun parseJsonList(info: String): JsonList {
    return DesktopJson.parseList(info)
}
