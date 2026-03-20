package app.linger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proximity_id_mappings")
data class ProximityIdMappingEntity(
    @PrimaryKey val scannedId: String,      // ephemeral BLE id
    val canonicalId: String,                  // user/venue id used in app/server
    val updatedAtMillis: Long
)
