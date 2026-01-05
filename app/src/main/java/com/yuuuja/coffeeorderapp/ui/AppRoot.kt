package com.yuuuja.coffeeorderapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yuuuja.coffeeorderapp.ui.cart.CartRoute
import com.yuuuja.coffeeorderapp.ui.home.HomeScreen
import com.yuuuja.coffeeorderapp.ui.detail.DetailScreen
import com.yuuuja.coffeeorderapp.ui.cart.CartScreen
import com.yuuuja.coffeeorderapp.ui.detail.DetailRoute
import com.yuuuja.coffeeorderapp.ui.home.HomeRoute
import com.yuuuja.coffeeorderapp.ui.orderInfo.OrderEntryType
import com.yuuuja.coffeeorderapp.ui.orderInfo.OrderRoute
import com.yuuuja.coffeeorderapp.ui.splash.SplashScreen
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel
import kotlin.enums.enumEntries

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    val cartViewModel: CartViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("home") {
           HomeRoute(
               navController = navController,
               cartContract = cartViewModel
           )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            DetailRoute(
                navController=navController,
                cartContract = cartViewModel,
                id = id
            )
        }
        composable("cart") {
            CartRoute(
                navController = navController,
                cartContract = cartViewModel
            )
        }
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("orderInfo/fromDetail") {
            OrderRoute(
                navController = navController,
                cartContract = cartViewModel,
                entryType = OrderEntryType.FROM_DETAIL
            )
        }

        composable("orderInfo/fromCart") {
            OrderRoute(
                navController = navController,
                cartContract = cartViewModel,
                entryType = OrderEntryType.FROM_CART
            )
        }
    }
}
