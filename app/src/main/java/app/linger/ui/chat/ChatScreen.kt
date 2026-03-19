package app.linger.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.linger.domain.model.ChatMode
import app.linger.ui.theme.ConnectionBlue
import app.linger.ui.theme.ConnectionGreen

@Composable
fun ChatScreen(
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var input by remember { mutableStateOf("") }

    val headerColor = when (state.mode) {
        ChatMode.LOCAL -> ConnectionBlue
        ChatMode.REMOTE -> ConnectionGreen
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = headerColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = if (state.mode == ChatMode.REMOTE) "Remote" else "Local",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        if (state.mode == ChatMode.LOCAL) {
            Button(
                onClick = { viewModel.keepInTouch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Keep in Touch")
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            items(
                items = state.messages,
                key = { it.id }
            ) { msg ->
                Text(
                    text = "${msg.sender}: ${msg.content}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message...") }
            )
            Button(
                onClick = {
                    viewModel.send(input)
                    input = ""
                }
            ) {
                Text("Send")
            }
        }
    }
}