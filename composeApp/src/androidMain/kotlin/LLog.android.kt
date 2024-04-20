import android.util.Log
import com.lollipop.wte.LogLevel

actual object LLog {
    actual fun p(level: LogLevel, tag: String, value: String) {
        when (level) {
            LogLevel.DEBUG -> {
                Log.d(tag, value)
            }

            LogLevel.INFO -> {
                Log.i(tag, value)
            }

            LogLevel.WARNING -> {
                Log.w(tag, value)
            }

            LogLevel.ERROR -> {
                Log.e(tag, value)
            }
        }
    }
}