package app.linger.ui.lounge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoungeScreen(
    onOpenChat: (String) -> Unit,
    viewModel: LoungeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "The Lounge",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = if (state.isScanning) "Scanning nearby..." else "Not scanning",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )

        LoungeRadarList(
            items = state.items,
            onGlance = { peerId ->
                viewModel.onGlance(peerId) { threadId ->
                    onOpenChat(threadId)
                }
            }
        )
    }
}