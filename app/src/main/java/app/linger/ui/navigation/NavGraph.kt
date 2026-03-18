package app.linger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.linger.ui.chat.ChatListScreen
import app.linger.ui.chat.ChatScreen
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
            ChatListScreen(
                onOpenThread = { threadId ->
                    navController.navigate("${Routes.CHAT}/$threadId")
                }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen()
        }
        composable(
            route = "${Routes.CHAT}/{${Routes.CHAT_THREAD_ID}}",
            arguments = listOf(navArgument(Routes.CHAT_THREAD_ID) { type = NavType.StringType })
        ) {
            ChatScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}