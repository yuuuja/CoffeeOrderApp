package com.yuuuja.coffeeorderapp.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CartIcon(
    totalQuantity: Int,
    onClick:() -> Unit
) {
    IconButton(onClick = onClick){
        BadgedBox(
            badge = {
                if(totalQuantity > 0) {
                    Badge{
                        Text(totalQuantity.toString())
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "장바구니"
            )
        }
    }

}