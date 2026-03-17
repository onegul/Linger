package app.linger.di

import app.linger.domain.usecase.CreateThreadUseCase
import app.linger.domain.usecase.GetNearbyProfilesUseCase
import app.linger.domain.usecase.ObserveBroadcastSettingsUseCase
import app.linger.domain.usecase.ObserveChatMessagesUseCase
import app.linger.domain.usecase.ObserveChatThreadsUseCase
import app.linger.domain.usecase.ObserveEncounterHistoryUseCase
import app.linger.domain.usecase.ObserveRemoteFriendsUseCase
import app.linger.domain.usecase.RefreshChatMessagesUseCase
import app.linger.domain.usecase.RefreshChatThreadsUseCase
import app.linger.domain.usecase.ScanNearbyUseCase
import app.linger.domain.usecase.SendMessagesUseCase
import app.linger.domain.usecase.UpdateBroadcastSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideScanNearbyUseCase(
        scanNearbyUseCase: ScanNearbyUseCase
    ): ScanNearbyUseCase = scanNearbyUseCase

    @Provides
    fun provideGetNearbyProfilesUseCase(
        useCase: GetNearbyProfilesUseCase
    ): GetNearbyProfilesUseCase = useCase

    @Provides
    fun provideObserveChatThreadsUseCase(
        useCase: ObserveChatThreadsUseCase
    ): ObserveChatThreadsUseCase = useCase

    @Provides
    fun provideObserveChatMessagesUseCase(
        useCase: ObserveChatMessagesUseCase
    ): ObserveChatMessagesUseCase = useCase

    @Provides
    fun provideRefreshChatThreadsUseCase(
        useCase: RefreshChatThreadsUseCase
    ): RefreshChatThreadsUseCase = useCase

    @Provides
    fun provideRefreshChatMessagesUseCase(
        useCase: RefreshChatMessagesUseCase
    ): RefreshChatMessagesUseCase = useCase

    @Provides
    fun provideSendMessageUseCase(
        useCase: SendMessagesUseCase
    ): SendMessagesUseCase = useCase

    @Provides
    fun provideCreateThreadUseCase(
        useCase: CreateThreadUseCase
    ): CreateThreadUseCase = useCase

    @Provides
    fun provideObserveEncounterHistoryUseCase(
        useCase: ObserveEncounterHistoryUseCase
    ): ObserveEncounterHistoryUseCase = useCase

    @Provides
    fun provideObserveRemoteFriendsUseCase(
        useCase: ObserveRemoteFriendsUseCase
    ): ObserveRemoteFriendsUseCase = useCase

    @Provides
    fun provideObserveBroadcastSettingsUseCase(
        useCase: ObserveBroadcastSettingsUseCase
    ): ObserveBroadcastSettingsUseCase = useCase

    @Provides
    fun provideUpdateBroadcastSettingsUseCase(
        useCase: UpdateBroadcastSettingsUseCase
    ): UpdateBroadcastSettingsUseCase = useCase
}