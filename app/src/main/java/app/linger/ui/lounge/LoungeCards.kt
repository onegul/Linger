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
import app.linger.domain.model.Encounter
import app.linger.domain.model.EncounterType
import app.linger.domain.model.ResonanceLevel

@Composable
fun LoungeRadarList(
    items: List<Encounter>,
    modifier: Modifier = Modifier,
    onGlance: (peerId: String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = items,
            key = { it.id }
        ) { encounter ->
            LoungeCard(encounter, onGlance)
        }
    }
}

@Composable
private fun LoungeCard(
    encounter: Encounter,
    onGlance: (peerId: String) -> Unit
) {
    val isResonant = encounter.resonance?.level == ResonanceLevel.HIGH
    val isVenue = encounter.type == EncounterType.VENUE

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
                text = when (encounter.type) {
                    EncounterType.USER -> if (isResonant) "Resonant profile" else "Profile nearby"
                    EncounterType.VENUE -> "Venue nearby"
                },
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "id: ${encounter.otherId}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = "distance: ${encounter.approximateDistanceMeters?.let { "%.0f".format(it) } ?: "?"}m",
                style = MaterialTheme.typography.bodySmall
            )

            val resonanceText =
                encounter.resonance?.let { "${"%.2f".format(it.value)} (${it.level})" } ?: "-"
            Text(
                text = "resonance: $resonanceText",
                style = MaterialTheme.typography.bodySmall
            )

            if (encounter.type == EncounterType.USER) {
                Button(
                    onClick = { onGlance(encounter.otherId) },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Glance")
                }
            }
        }
    }
}