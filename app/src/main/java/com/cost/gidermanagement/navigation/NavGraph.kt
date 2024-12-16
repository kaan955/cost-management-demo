package com.demo.gidermanagement.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.demo.gidermanagement.ui.detailList.DetailListScreen
import com.demo.gidermanagement.ui.home.HomeScreen
import com.demo.gidermanagement.ui.home.HomeViewModel
import com.demo.gidermanagement.ui.settings.preferences.PreferencesScreen
import com.demo.gidermanagement.ui.settings.SettingsContent
import com.demo.gidermanagement.ui.settings.SettingsScreen
import com.demo.gidermanagement.ui.settings.categories.CategoriesScreen
import com.demo.gidermanagement.ui.settings.notifications.NotificationsScreen
import com.demo.gidermanagement.ui.transactions.AddTransactionScreen

@Composable
fun NavGraph(
) {
     val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Screen.HomeScreen) {
        composable<Screen.HomeScreen> {
            val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                homeViewModel,
                navigateToTransaction = {itemID ->
                    navController.navigate(Screen.AddTransactionScreen(transactionID = itemID))
                },
                navigateToBottomItem = {navController.navigate(it)},
            )
        }

        composable<Screen.AddTransactionScreen> { backStackEntry ->
            AddTransactionScreen(
                navigateToScreen   = { screen ->
                    navController.navigate(screen)
                },
            )
        }
        composable<Screen.DetailListScreen> {
            DetailListScreen(
                navigateToBottomItem = {
                    navController.navigate(it)
                },
                navigateToTransaction = {itemID ->
                    navController.navigate(Screen.AddTransactionScreen(transactionID = itemID))
                }
            )
        }
        composable<Screen.SettingsScreen> {
            SettingsScreen(
                navigateToItem = {
                    navController.navigate(it)
                }
            )
        }
        composable<Screen.PreferencesScreen> {
            PreferencesScreen(
                navigation = {
                    navController.navigate(it)
                }
            )
        }
        composable<Screen.NotificationsScreen> {
            NotificationsScreen(
                onbackClicked = {
                    navController.navigate(Screen.SettingsScreen)
                }
            )
        }

        composable<Screen.CategoriesScreen> {
            CategoriesScreen(
                onbackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }

}
