package app.linger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.linger.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): UserProfileEntity?

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<UserProfileEntity?>
}