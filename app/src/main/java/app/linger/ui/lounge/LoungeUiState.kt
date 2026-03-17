package app.linger.ui.lounge

import app.linger.domain.model.Encounter

data class LoungeUiState(
    val isScanning: Boolean,
    val items: List<Encounter>
)
