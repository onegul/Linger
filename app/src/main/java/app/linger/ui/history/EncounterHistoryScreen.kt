package app.linger.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.linger.domain.model.Encounter
import app.linger.domain.model.EncounterType

@Composable
fun EncounterHistoryScreen(
    onOpenChat: (String) -> Unit,
    viewModel: EncounterHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Past Crossings",
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            modifier = Modifier.padding(12.dp)
        ) {
            items(
                items = state.items,
                key = { it.id }
            ) { encounter ->
                EncounterHistoryRow(
                    encounter = encounter,
                    onOpenChat = onOpenChat
                )
            }
        }
    }
}

@Composable
private fun EncounterHistoryRow(
    encounter: Encounter,
    onOpenChat: (String) -> Unit
) {
    val title = when (encounter.type) {
        EncounterType.USER -> "Person you crossed"
        EncounterType.VENUE -> "Venue you crossed"
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)

        Text(
            text = "id: ${encounter.otherId}",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "remote friend: ${encounter.isRemoteFriend}",
            style = MaterialTheme.typography.bodySmall
        )

        if (encounter.type == EncounterType.USER) {
            Text(
                text = "Tap to chat again",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .then(
                        Modifier
                            .padding(end = 8.dp)
                            .fillMaxSize()      // optional; will make this clickable later
                    )
            )
        }
    }
}