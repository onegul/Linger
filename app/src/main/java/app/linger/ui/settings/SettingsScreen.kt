package app.linger.ui.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val permissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_CONNECT)
            add(Manifest.permission.BLUETOOTH_ADVERTISE)
        } else
            add(Manifest.permission.BLUETOOTH)
    }

    var pendingEnabled by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantedMap ->
        val allGranted = permissions.all { grantedMap[it] == true }
        if (allGranted)
            viewModel.setShowLifeToPeople(pendingEnabled)
        // If not granted, do nothing; Switch stays controlled by state from DataStore.
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Presence & Privacy",
            style = MaterialTheme.typography.headlineSmall
        )

        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Show life to people")
            Switch(
                checked = state.settings.showLifeToPeople,
                onCheckedChange = { enabled ->
                    if (!enabled) {
                        // Turning off doesn't require permissions.
                        viewModel.setShowLifeToPeople(false)
                        return@Switch
                    }

                    // Turning on requires permissions.
                    pendingEnabled = true
                    permissionLauncher.launch(permissions.toTypedArray())
                }
            )
        }

        TextButton(
            onClick = {
                // TODO: If permissions can be asked in-app, then ask here or open system settings or
                // show which permissions are granted/revoked or show explanation dialog
            },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(text = "Manage system permissions")
        }
    }
}