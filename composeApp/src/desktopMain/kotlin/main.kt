import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.lollipop.wte.App
import com.lollipop.wte.Config

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = Config.APP_NAME) {
        App(PaddingValues(0.dp))
    }
}