package app.linger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.linger.ui.lounge.LoungeScreen
import app.linger.ui.settings.SettingsScreen

@Composable
fun LingerNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOUNGE
    ) {
        composable(Routes.LOUNGE) {
            LoungeScreen()
        }
        composable(Routes.CHAT_LIST) {

        }
        composable(Routes.SETTINGS) {
            SettingsScreen()
        }
    }
}