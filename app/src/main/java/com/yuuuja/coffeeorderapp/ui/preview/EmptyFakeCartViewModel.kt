package com.yuuuja.coffeeorderapp.ui.preview

import com.yuuuja.coffeeorderapp.model.CartItem
import com.yuuuja.coffeeorderapp.viewmodel.CartContract

class EmptyFakeCartViewModel : CartContract {
    override val items: List<CartItem> get() = emptyList()

    override fun add(item: CartItem) {}
    override fun remove(id: Long) {}
    override fun updateQuantity(id: Long, quantity: Int) {}
}