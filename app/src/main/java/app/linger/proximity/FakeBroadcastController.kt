package app.linger.proximity

import app.linger.domain.model.BroadcastSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBroadcastController : BroadcastController {
    private val _state = MutableStateFlow(BroadcastState.OFF)
    override val state: Flow<BroadcastState> = _state

    override suspend fun enableBroadcasting(settings: BroadcastSettings) {
        _state.value = BroadcastState.ON
    }

    override suspend fun disableBroadcasting() {
        _state.value = BroadcastState.OFF
    }
}