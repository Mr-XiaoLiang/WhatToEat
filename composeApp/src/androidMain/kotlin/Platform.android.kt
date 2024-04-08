import android.os.Build
import org.lollipop.wte.LApplication
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val fileDir: File by lazy {
        LApplication.application.filesDir
    }

}

actual fun getPlatform(): Platform = AndroidPlatform()