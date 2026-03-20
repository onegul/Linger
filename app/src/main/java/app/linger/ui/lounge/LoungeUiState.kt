package app.linger.ui.lounge

data class LoungeUiState(
    val isScanning: Boolean,
    val items: List<LoungeItem>
)
