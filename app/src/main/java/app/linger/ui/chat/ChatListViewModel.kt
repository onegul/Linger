package app.linger.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linger.domain.usecase.ObserveChatThreadsUseCase
import app.linger.domain.usecase.RefreshChatThreadsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    observeChatThreads: ObserveChatThreadsUseCase,
    private val refreshChatThreads: RefreshChatThreadsUseCase
) : ViewModel() {
    val uiState: StateFlow<ChatListUiState> =
        observeChatThreads()
            .map { threads ->
                ChatListUiState(
                    isLoading = false,
                    threads = threads
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChatListUiState(isLoading = true, threads = emptyList())
            )

    fun refresh() {
        viewModelScope.launch { refreshChatThreads() }
    }
}