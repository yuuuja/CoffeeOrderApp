package com.yuuuja.coffeeorderapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yuuuja.coffeeorderapp.ui.home.HomeScreen
import com.yuuuja.coffeeorderapp.ui.detail.DetailScreen
import com.yuuuja.coffeeorderapp.ui.cart.CartScreen
import com.yuuuja.coffeeorderapp.ui.splash.SplashScreen

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            DetailScreen(navController = navController, id = id)
        }
        composable("cart") {
            CartScreen(navController = navController)
        }
        composable("splash") {
            SplashScreen(navController = navController)
        }
    }
}
