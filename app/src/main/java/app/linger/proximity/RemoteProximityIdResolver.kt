package app.linger.proximity

import app.linger.data.local.dao.ProximityIdMappingDao
import app.linger.data.local.entity.ProximityIdMappingEntity
import app.linger.data.remote.api.ProfileApi
import app.linger.data.remote.api.ResolveProximityIdBody
import javax.inject.Inject
import kotlin.time.Clock

class RemoteProximityIdResolver @Inject constructor(
    private val mappingDao: ProximityIdMappingDao,
    private val profileApi: ProfileApi
) : ProximityIdResolver {
    override suspend fun resolve(scannedId: String): String? {
        val cached = mappingDao.getByScannedId(scannedId)
        if (cached != null) return cached.canonicalId

        return try {
            val remote = profileApi.resolveProximityId(
                ResolveProximityIdBody(scannedId = scannedId)
            )
            val canonical = remote.canonicalId
            mappingDao.upsert(
                ProximityIdMappingEntity(
                    scannedId = scannedId,
                    canonicalId = canonical,
                    updatedAtMillis = Clock.System.now().toEpochMilliseconds()
                )
            )
            canonical
        } catch (_: Throwable) {
            null
        }
    }
}