package com.yuuuja.coffeeorderapp.ui.detail

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel

@Composable
fun DetailRoute(
    navController: NavController,
    id: Long
) {
    val cartViewModel: CartViewModel = viewModel()

    DetailScreen(
        navController = navController,
        id = id,
        cartContract = cartViewModel
    )
}