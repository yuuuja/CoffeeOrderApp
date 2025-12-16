package com.yuuuja.coffeeorderapp.viewmodel

import com.yuuuja.coffeeorderapp.model.CartItem

interface CartContract {
    val items: List<CartItem>
    fun add(item: CartItem)
    fun remove(id: Long)
    fun updateQuantity(id: Long, quantity: Int)
}