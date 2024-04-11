import com.lollipop.wte.info.json.JsonInfo
import com.lollipop.wte.info.json.JsonList
import com.lollipop.wte.json.DesktopJson
import com.lollipop.wte.preferences.DesktopPreferences
import com.lollipop.wte.preferences.LPreferences
import java.io.File
import java.util.prefs.Preferences

actual object Platform {
    actual val name: String = "Java ${System.getProperty("java.version")}"
    actual val fileDir: File by lazy {
        File(System.getProperty("user.home"), Config.APP_NAME).apply {
            mkdirs()
        }
    }

    actual fun parseJsonInfo(info: String): JsonInfo {
        return DesktopJson.parseInfo(info)
    }

    actual fun parseJsonList(info: String): JsonList {
        return DesktopJson.parseList(info)
    }

    actual fun getPreferences(name: String): LPreferences {
        return DesktopPreferences(Preferences.userRoot().node("wte").node(name))
    }
}


