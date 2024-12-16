package com.demo.gidermanagement.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class Screen {
    @Serializable
    data object HomeScreen: Screen()


    @Serializable
    data class AddTransactionScreen(
        val transactionID: Int
    ): Screen()
    @Serializable
    data object SettingsScreen: Screen()

    @Serializable
    data object DetailListScreen: Screen()

    @Serializable
    data object PreferencesScreen: Screen()

    @Serializable
    data object NotificationsScreen: Screen()

    @Serializable
    data object CategoriesScreen: Screen()
}
