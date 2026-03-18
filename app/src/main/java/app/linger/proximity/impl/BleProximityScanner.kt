package app.linger.proximity.impl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import app.linger.proximity.ProximityScanner
import app.linger.proximity.model.ProximityHit
import app.linger.proximity.model.ProximityTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Clock

class BleProximityScanner(
    private val context: Context
) : ProximityScanner {
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

            // TODO: parse your payload to extract targetId + type
            val targetId = result.device.address        // placeholder (NOT privacy-safe)
            val type = ProximityTargetType.USER

            val existing = hitMap[targetId]
            hitMap[targetId] = existing?.copy(
                rssi = rssi,
                lastSeenAt = now
            ) ?: ProximityHit(
                targetId = targetId,
                type = type,
                distanceMeters = null,
                rssi = rssi,
                firstSeenAt = now,
                lastSeenAt = now
            )

            _hits.value = hitMap.values.toList()
        }
    }

    override fun startScanning() {
        if (!scanning || scanner == null) return
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