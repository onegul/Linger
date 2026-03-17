package app.linger.di

import app.linger.data.repository.chat.ChatRepository
import app.linger.data.repository.chat.ChatRepositoryImpl
import app.linger.data.repository.encounter.EncounterRepository
import app.linger.data.repository.encounter.EncounterRepositoryImpl
import app.linger.data.repository.profile.ProfileRepository
import app.linger.data.repository.profile.ProfileRepositoryImpl
import app.linger.data.repository.venue.VenueRepository
import app.linger.data.repository.venue.VenueRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindEncounterRepository(impl: EncounterRepositoryImpl): EncounterRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindVenueRepository(impl: VenueRepositoryImpl): VenueRepository
}