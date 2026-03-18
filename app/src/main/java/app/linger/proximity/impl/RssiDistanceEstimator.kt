package app.linger.proximity.impl

import kotlin.math.pow

object RssiDistanceEstimator {
    /**
     * Rough estimate in meters, or null if RSSI is invalid.
     * txPowerDbm: RSSI at 1m (typical -59 ... -65)
     */
    fun estimateMeters(rssi: Int?, txPowerDbm: Int = -59, n: Double = 2.0): Double? {
        if (rssi == null || rssi == 0) return null
        val ratio = (txPowerDbm - rssi) / (10.0 * n)
        return 10.0.pow(ratio)
    }
}