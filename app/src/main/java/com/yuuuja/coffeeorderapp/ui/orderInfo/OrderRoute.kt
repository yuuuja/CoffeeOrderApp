package com.yuuuja.coffeeorderapp.ui.orderInfo

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.viewmodel.CartContract

@Composable
fun OrderRoute(
    navController: NavController,
    cartContract: CartContract,
    entryType: OrderEntryType
) {
    OrderInfoScreen(
        navController = navController,
        cart = cartContract,
        entryType = entryType
    )
}