package app.linger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.linger.sync.SyncScheduler
import app.linger.ui.navigation.LingerNavGraph
import app.linger.ui.theme.LingerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SyncScheduler.schedule(applicationContext)

        setContent {
            LingerAppRoot()
        }
    }
}

@Composable
fun LingerAppRoot() {
    LingerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LingerNavGraph()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LingerAppRoot()
}