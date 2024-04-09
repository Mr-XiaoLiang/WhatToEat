import android.os.Build
import com.lollipop.wte.json.JsonInfo
import com.lollipop.wte.json.JsonList
import org.lollipop.wte.LApplication
import org.lollipop.wte.json.AndroidJson
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val fileDir: File by lazy {
        LApplication.application.filesDir
    }

}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun parseJsonInfo(info: String): JsonInfo {
    return AndroidJson.parseInfo(info)
}

actual fun parseJsonList(info: String): JsonList {
    return AndroidJson.parseList(info)
}
