package com.yuuuja.coffeeorderapp.ui.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel

@Composable
fun HomeRoute(navController: NavController) {
    val cartViewModel: CartViewModel = viewModel()

    HomeScreen(
        navController = navController,
        cartContract = cartViewModel
    )
}