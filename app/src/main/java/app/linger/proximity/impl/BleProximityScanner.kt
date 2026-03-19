package app.linger.proximity.impl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.ParcelUuid
import app.linger.proximity.ProximityScanner
import app.linger.proximity.model.ProximityHit
import app.linger.proximity.model.ProximityTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import kotlin.time.Clock

class BleProximityScanner(
    private val context: Context
) : ProximityScanner {
    private val serviceUuid: UUID = UUID.fromString("0000FEED-0000-1000-8000-00805F9B34FB")

    private val _hits = MutableStateFlow<List<ProximityHit>>(emptyList())
    override val proximityHits: Flow<List<ProximityHit>> = _hits

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val mgr = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mgr.adapter
    }

    private val scanner: BluetoothLeScanner?
        get() = bluetoothAdapter?.bluetoothLeScanner

    private var scanning = false

    private val hitMap = LinkedHashMap<String, ProximityHit>()  // targetId -> aggregated hit

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val now = Clock.System.now()
            val rssi = result.rssi

            val scanRecord = result.scanRecord ?: return
            val rawPayload = scanRecord.getServiceData(ParcelUuid(serviceUuid)) ?: return
            val decoded = decodePayload(rawPayload) ?: return

            val targetId = decoded.targetId
            val type = decoded.type
            val distance = RssiDistanceEstimator.estimateMeters(rssi)

            val existing = hitMap[targetId]
            hitMap[targetId] = existing?.copy(
                distanceMeters = distance,
                rssi = rssi,
                lastSeenAt = now
            ) ?: ProximityHit(
                targetId = targetId,
                type = type,
                distanceMeters = distance,
                rssi = rssi,
                firstSeenAt = now,
                lastSeenAt = now
            )

            _hits.value = hitMap.values.toList()
        }
    }

    override fun startScanning() {
        if (scanning) return

        val adapter = bluetoothAdapter ?: return
        if (!adapter.isEnabled) return

        val scanner = scanner ?: return
        scanning = true
        // TODO: add ScanFilters + ScanSettings (Low Latency vs balanced)
        scanner?.startScan(callback)
    }

    override fun stopScanning() {
        if (!scanning) return
        scanning = false
        scanner?.stopScan(callback)
        hitMap.clear()
        _hits.value = emptyList()
    }
}