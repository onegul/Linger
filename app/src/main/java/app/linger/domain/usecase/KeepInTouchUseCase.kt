package app.linger.domain.usecase

import app.linger.core.util.Result
import app.linger.data.repository.chat.ChatRepository
import app.linger.data.repository.encounter.EncounterRepository
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread
import javax.inject.Inject

class KeepInTouchUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(threadId: String): Result<ChatThread> {
        val result = chatRepository.setThreadMode(threadId = threadId, mode = ChatMode.REMOTE)

        return when (result) {
            is Result.Success -> {
                encounterRepository.markRemoteFriend(result.value.peerId, true)
                result
            }

            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}