import com.lollipop.wte.LogLevel

expect object LLog {

    fun p(level: LogLevel, tag: String, value: String)

}

fun logger(
    level: LogLevel = LogLevel.DEBUG,
    tag: String = "WTE"
): (String) -> Unit = { value ->
    LLog.p(level, tag, value)
}

inline fun <reified T> T.loggerOf(
    level: LogLevel = LogLevel.DEBUG,
    tag: String = "WTE"
): (String) -> Unit = { value ->
    val simpleName = T::class.java.simpleName
    LLog.p(level, tag, "$simpleName --> $value")
}
