package com.yuuuja.coffeeorderapp.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.viewmodel.CartContract

@Composable
fun HomeRoute(navController: NavController, cartContract: CartContract) {

    HomeScreen(
        navController = navController,
        cartContract = cartContract
    )
}