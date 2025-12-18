package com.yuuuja.coffeeorderapp.ui.detail

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.viewmodel.CartContract
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel

@Composable
fun DetailRoute(
    navController: NavController,
    cartContract: CartContract,
    id: Long
) {

    DetailScreen(
        navController = navController,
        id = id,
        cartContract = cartContract
    )
}