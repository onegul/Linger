package app.linger.ui.history

import app.linger.domain.model.Encounter

data class EncounterHistoryUiState(
    val items: List<Encounter>
)
