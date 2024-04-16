import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lollipop.wte.DataHelper
import com.lollipop.wte.Initialize
import com.lollipop.wte.ui.ContentPage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(padding: PaddingValues) {
    Initialize.init()

    val coroutineScope = rememberCoroutineScope()
    var currentDataState by remember { mutableStateOf(DataHelper.State.IDLE) }
    val dataHelper = remember {
        DataHelper(coroutineScope) {
            currentDataState = it
        }
    }
    ContentPage(padding, dataHelper)
}