package app.linger.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.linger.data.repository.chat.ChatRepository
import app.linger.data.repository.encounter.EncounterRepository
import app.linger.data.repository.profile.ProfileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val profileRepository: ProfileRepository,
    private val encounterRepository: EncounterRepository,
    private val chatRepository: ChatRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            // TODO: "me" will be replaced with authenticated id when auth is added
            profileRepository.refreshProfile(id = "me")
            encounterRepository.syncFromRemote()

            chatRepository.refreshThreads()

            // Refresh messages for each known thread so chat is populated offline-first
            val threads = chatRepository.observeThreads().first()
            threads.forEach { thread ->
                chatRepository.refreshMessages(thread.id)
            }

            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}