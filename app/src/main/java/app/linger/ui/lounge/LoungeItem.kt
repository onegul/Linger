package app.linger.ui.lounge

import app.linger.domain.model.EncounterType
import app.linger.domain.model.ResonanceLevel

data class LoungeItem(
    val encounterId: String,
    val peerId: String,
    val type: EncounterType,
    val resonanceLevel: ResonanceLevel?,
    val distanceMeters: Double?,
    val essencePrimary: String,
    val essenceSecondary: String
)
