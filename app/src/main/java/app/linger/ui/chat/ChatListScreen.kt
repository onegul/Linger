package app.linger.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.linger.domain.model.ChatMode

@Composable
fun ChatListScreen(
    onOpenThread: (String) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "The Circle",
            style = MaterialTheme.typography.headlineMedium
        )

        TextButton(
            onClick = { viewModel.refresh() },
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text("Refresh")
        }

        LazyColumn {
            items(
                items = state.threads,
                key = { it.id }
            ) { thread ->
                ChatThreadRow(
                    title = if (thread.mode == ChatMode.REMOTE) "Remote Aura" else "Local Aura",
                    subtitle = thread.lastMessagePreview ?: "No messages yet",
                    mode = thread.mode,
                    onClick = { onOpenThread(thread.id) }
                )
            }
        }
    }
}

@Composable
private fun ChatThreadRow(
    title: String,
    subtitle: String,
    mode: ChatMode,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = if (mode == ChatMode.REMOTE) "Remote" else "Local",
            style = MaterialTheme.typography.labelSmall
        )
    }
}