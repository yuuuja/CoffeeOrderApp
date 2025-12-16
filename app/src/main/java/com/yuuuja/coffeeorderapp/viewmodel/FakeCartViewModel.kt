package com.yuuuja.coffeeorderapp.viewmodel
import com.yuuuja.coffeeorderapp.model.CartItem

class FakeCartViewModel : CartContract {
    private val _items = mutableListOf<CartItem>()
    override val items: List<CartItem> get() = _items

    override fun add(item: CartItem) {
        _items.add(item)
    }
    override fun remove(id: Long) {}
    override fun updateQuantity(id: Long, quantity: Int) {}

    fun clear() {
        _items.clear()
    }
}