package app.linger.data.local.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.linger.domain.model.BroadcastSettings
import app.linger.domain.model.Discoverability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "linger_settings")

class SettingsDataStore(private val context: Context) {
    private val prefs = context.dataStore

    val broadcastSettings: Flow<BroadcastSettings> = prefs.data.map { prefs ->
        BroadcastSettings(
            discoverability = prefs[KEY_DISCOVERABILITY]?.let {
                Discoverability.valueOf(it)
            } ?: Discoverability.ALL,
            showLifeToPeople = prefs[KEY_SHOW_TO_PEOPLE] ?: true
        )
    }

    suspend fun updateBroadcastSettings(block: (BroadcastSettings) -> BroadcastSettings) {
        prefs.edit { edit ->
            val current = BroadcastSettings(
                discoverability = edit[KEY_DISCOVERABILITY]?.let {
                    Discoverability.valueOf(it)
                } ?: Discoverability.ALL,
                showLifeToPeople = edit[KEY_SHOW_TO_PEOPLE] ?: true
            )

            val updated = block(current)
            edit[KEY_DISCOVERABILITY] = updated.discoverability.name
            edit[KEY_SHOW_TO_PEOPLE] = updated.showLifeToPeople
        }
    }

    companion object {
        private val KEY_DISCOVERABILITY = stringPreferencesKey("discoverability")
        private val KEY_SHOW_TO_PEOPLE = booleanPreferencesKey("show_life_to_people")
    }
}