package com.yuuuja.coffeeorderapp.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.yuuuja.coffeeorderapp.ui.cart.CartScreen

@Preview(showBackground = true)
@Composable
fun CartPreview_WithItems() {
    CartScreen(
        navController = rememberNavController(),
        cart = FakeCartViewModel()
    )
}

@Preview(showBackground = true)
@Composable
fun CartPreview_Empty() {
    CartScreen(
        navController = rememberNavController(),
        cart = EmptyFakeCartViewModel()
    )
}