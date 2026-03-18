package app.linger

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LingerApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workManagerConfig: Configuration

    override val workManagerConfiguration: Configuration
        get() = workManagerConfig
}