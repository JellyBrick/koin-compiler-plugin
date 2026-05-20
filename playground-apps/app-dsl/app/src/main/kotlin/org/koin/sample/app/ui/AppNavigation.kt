package org.koin.sample.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.sample.feature.bookmarks.BookmarksScreen
import org.koin.sample.feature.detail.DetailScreen
import org.koin.sample.feature.home.HomeScreen
import org.koin.sample.feature.settings.SettingsScreen

@Serializable object HomeRoute
@Serializable object BookmarksRoute
@Serializable object SettingsRoute
@Serializable data class DetailRoute(val newsId: String)

@Composable
fun AppNavigation(isDarkTheme: Boolean) {
    val navController = rememberNavController()

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme(),
    ) {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<HomeRoute>() } == true,
                        onClick = { navController.navigate(HomeRoute) { popUpTo(HomeRoute) { inclusive = true } } },
                        icon = { Text("H") },
                        label = { Text("Home") },
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<BookmarksRoute>() } == true,
                        onClick = { navController.navigate(BookmarksRoute) { popUpTo(HomeRoute) } },
                        icon = { Text("B") },
                        label = { Text("Bookmarks") },
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<SettingsRoute>() } == true,
                        onClick = { navController.navigate(SettingsRoute) { popUpTo(HomeRoute) } },
                        icon = { Text("S") },
                        label = { Text("Settings") },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable<HomeRoute> {
                    HomeScreen(onNewsClick = { newsId -> navController.navigate(DetailRoute(newsId)) })
                }
                composable<BookmarksRoute> {
                    BookmarksScreen()
                }
                composable<SettingsRoute> {
                    SettingsScreen()
                }
                composable<DetailRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<DetailRoute>()
                    DetailScreen(newsId = route.newsId)
                }
            }
        }
    }
}
