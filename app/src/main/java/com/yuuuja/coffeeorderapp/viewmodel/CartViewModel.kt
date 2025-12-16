package com.yuuuja.coffeeorderapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.yuuuja.coffeeorderapp.model.CartItem

class CartViewModel : ViewModel(), CartContract {

    private val _items = mutableStateListOf<CartItem>()
    override val items: List<CartItem> get() = _items

    override fun add(item: CartItem) {
        val idx = _items.indexOfFirst {
            it.menu.id == item.menu.id &&
                    it.cup == item.cup &&
                    it.temp == item.temp &&
                    it.size == item.size &&
                    it.shot == item.shot
        }
        if (idx >= 0) {
            val old = _items[idx]
            _items[idx] = old.copy(quantity = old.quantity + item.quantity)
        } else {
            _items.add(item)
        }
    }

    override fun remove(id: Long) {
        _items.removeIf { it.id == id }
    }

    override fun updateQuantity(id: Long, quantity: Int) {
        _items.find { it.id == id }?.let {
            val index = _items.indexOf(it)
            _items[index] = it.copy(quantity = quantity)
        }
    }

    fun clear() = _items.clear()

}