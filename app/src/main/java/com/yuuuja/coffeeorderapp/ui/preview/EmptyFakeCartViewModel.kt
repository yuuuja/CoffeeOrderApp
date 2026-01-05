package com.yuuuja.coffeeorderapp.ui.preview

import androidx.compose.runtime.mutableStateListOf
import com.yuuuja.coffeeorderapp.model.CartItem
import com.yuuuja.coffeeorderapp.viewmodel.CartContract

class EmptyFakeCartViewModel : CartContract {
    private val _items = mutableStateListOf<CartItem>()
    override val items: List<CartItem> get() = emptyList()

    override fun add(item: CartItem) {}
    override fun remove(id: Long) {}
    override fun updateQuantity(id: Long, quantity: Int) {}
    override fun clear() {_items.clear()}
}