package app.linger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.linger.data.local.entity.ProximityIdMappingEntity

@Dao
interface ProximityIdMappingDao {
    @Query("SELECT * FROM proximity_id_mappings WHERE scannedId = :scannedId LIMIT 1")
    suspend fun getByScannedId(scannedId: String): ProximityIdMappingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mapping: ProximityIdMappingEntity)
}