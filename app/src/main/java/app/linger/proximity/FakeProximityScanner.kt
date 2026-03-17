package app.linger.proximity

import app.linger.proximity.model.ProximityHit
import app.linger.proximity.model.ProximityTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Clock

class FakeProximityScanner : ProximityScanner {
    private val _hits = MutableStateFlow<List<ProximityHit>>(emptyList())
    override val proximityHits: Flow<List<ProximityHit>> = _hits

    override fun startScanning() {
        _hits.value = listOf(
            ProximityHit(
                targetId = "demo-user",
                type = ProximityTargetType.USER,
                distanceMeters = 5.0,
                rssi = -60,
                firstSeenAt = Clock.System.now(),
                lastSeenAt = Clock.System.now()
            )
        )
    }

    override fun stopScanning() {
        _hits.value = emptyList()
    }
}