package app.linger.proximity.impl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import app.linger.domain.model.BroadcastSettings
import app.linger.proximity.BroadcastController
import app.linger.proximity.BroadcastState
import app.linger.proximity.model.ProximityTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class BleBroadcastController(
    private val context: Context,
    private val ephemeralIdProvider: EphemeralIdProvider
) : BroadcastController {
    private val serviceUuid =
        UUID.fromString("0000FEED-0000-1000-8000-00805F9B34FB")   // placeholder UUID

    private val _state = MutableStateFlow(BroadcastState.OFF)
    override val state: Flow<BroadcastState> = _state

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val mgr = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mgr.adapter
    }

    private val advertiser: BluetoothLeAdvertiser?
        get() = bluetoothAdapter?.bluetoothLeAdvertiser

    private var callback: AdvertiseCallback? = null

    override suspend fun enableBroadcasting(settings: BroadcastSettings) {
        if (!settings.showLifeToPeople) {
            disableBroadcasting()
            return
        }

        val adapter = bluetoothAdapter ?: return
        if (!adapter.isEnabled) return

        val adv = advertiser ?: return

        val id = ephemeralIdProvider.currentEphemeralId()
        val payload = encodePayload(
            type = ProximityTargetType.USER,
            ephemeralId = id
        )

        val advertiseSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setConnectable(false)
            .build()

        val data = AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid(serviceUuid))
            .addServiceData(ParcelUuid(serviceUuid), payload)
            .setIncludeDeviceName(false)
            .build()

        callback = object : AdvertiseCallback() {}
        adv.startAdvertising(advertiseSettings, data, callback)
        _state.value = BroadcastState.ON
    }

    override suspend fun disableBroadcasting() {
        callback?.let { advertiser?.stopAdvertising(it) }
        callback = null
        _state.value = BroadcastState.OFF
    }
}