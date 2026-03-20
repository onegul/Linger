package app.linger.ui.lounge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.linger.domain.model.EncounterType
import app.linger.domain.model.ResonanceLevel

@Composable
fun LoungeRadarList(
    items: List<LoungeItem>,
    modifier: Modifier = Modifier,
    onGlance: (peerId: String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = items,
            key = { it.encounterId }
        ) { encounter ->
            LoungeCard(encounter, onGlance)
        }
    }
}

@Composable
private fun LoungeCard(
    item: LoungeItem,
    onGlance: (peerId: String) -> Unit
) {
    val isResonant = item.resonanceLevel == ResonanceLevel.HIGH
    val isVenue = item.type == EncounterType.VENUE

    val containerColor = when {
        isVenue -> MaterialTheme.colorScheme.error
        isResonant -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.secondary
    }

    val contentColor = when {
        isVenue -> MaterialTheme.colorScheme.onError
        isResonant -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onSecondary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.essencePrimary,
                style = MaterialTheme.typography.titleMedium
            )

            if (item.essenceSecondary.isNotBlank())
                Text(
                    text = item.essenceSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )

            item.distanceMeters?.let { d ->
                Text(
                    text = "≈ ${"%.0f".format(d)}m",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            if (item.type == EncounterType.USER) {
                Button(
                    onClick = { onGlance(item.peerId) },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Glance")
                }
            }
        }
    }
}