import com.lollipop.wte.LogLevel

actual object LLog {
    actual fun p(level: LogLevel, tag: String, value: String) {
        when (level) {
            LogLevel.DEBUG -> {
                println("$tag : D : $value")
            }

            LogLevel.INFO -> {
                println("$tag : I : $value")
            }

            LogLevel.WARNING -> {
                System.err.println("$tag : W : $value")
            }

            LogLevel.ERROR -> {
                System.err.println("$tag : E : $value")
            }
        }
    }
}