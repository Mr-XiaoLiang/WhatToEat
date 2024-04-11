import android.content.Context
import android.os.Build
import com.lollipop.wte.LApplication
import com.lollipop.wte.info.json.JsonInfo
import com.lollipop.wte.info.json.JsonList
import com.lollipop.wte.json.AndroidJson
import com.lollipop.wte.preferences.AndroidPreferences
import com.lollipop.wte.preferences.LPreferences
import java.io.File

actual object Platform {

    actual val name: String = "Android ${Build.VERSION.SDK_INT}"

    actual val fileDir: File by lazy {
        LApplication.application.filesDir
    }

    actual fun parseJsonInfo(info: String): JsonInfo {
        return AndroidJson.parseInfo(info)
    }

    actual fun parseJsonList(info: String): JsonList {
        return AndroidJson.parseList(info)
    }

    actual fun getPreferences(name: String): LPreferences {
        val sharedPreferences = LApplication.application.getSharedPreferences(
            name, Context.MODE_PRIVATE
        )
        return AndroidPreferences(sharedPreferences)
    }

}
