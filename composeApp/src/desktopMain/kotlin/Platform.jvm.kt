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