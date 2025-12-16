package com.yuuuja.coffeeorderapp.ui.cart

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel

@Composable
fun CartRoute(navController: NavController) {
    val cartViewModel: CartViewModel = viewModel()

    CartScreen(cart = cartViewModel, onBack = { navController.popBackStack() })

}