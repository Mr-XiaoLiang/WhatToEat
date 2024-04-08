import java.io.File

interface Platform {
    val name: String

    val fileDir: File

}

expect fun getPlatform(): Platform