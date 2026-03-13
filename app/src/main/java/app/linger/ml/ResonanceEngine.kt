package app.linger.ml

import app.linger.domain.model.Profile
import app.linger.domain.model.ResonanceScore

interface ResonanceEngine {
    /**
     * Computes how "resonant" the other profile is relative to the current user, based only on
     * local data.
     */
    fun computeResonance(self: Profile, other: Profile): ResonanceScore
}