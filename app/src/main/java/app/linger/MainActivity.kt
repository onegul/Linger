package app.linger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.linger.ui.theme.LingerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LingerAppRoot()
        }
    }
}

@Composable
fun LingerAppRoot() {
    LingerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Text(text = "Linger - Phase 0")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LingerAppRoot()
}