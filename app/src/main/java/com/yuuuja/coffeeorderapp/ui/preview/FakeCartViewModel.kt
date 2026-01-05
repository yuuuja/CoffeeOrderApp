package com.yuuuja.coffeeorderapp.ui.preview

import com.yuuuja.coffeeorderapp.model.CartItem
import com.yuuuja.coffeeorderapp.model.CupType
import com.yuuuja.coffeeorderapp.model.DrinkSize
import com.yuuuja.coffeeorderapp.model.Temperature
import com.yuuuja.coffeeorderapp.rules.ShotOption
import com.yuuuja.coffeeorderapp.viewmodel.CartContract

class FakeCartViewModel : CartContract {
    private val _items = mutableListOf<CartItem>(
        CartItem(
            id = 1L,
            menu = FakeMenus.americano,
            quantity = 1,
            cup = CupType.PERSONAL,
            temp = Temperature.HOT,
            size = DrinkSize.L,
            shot = ShotOption.PLUS1SHOT
        ),
        CartItem(
            id=2L,
            menu = FakeMenus.strawberryLatte,
            quantity = 2,
            cup = CupType.DISPOSABLE,
            temp = Temperature.ICE,
            size = DrinkSize.M,
            shot = ShotOption.NONE
            )
    )
    override val items: List<CartItem> get() = _items

    override fun add(item: CartItem) {
        _items.add(item)
    }
    override fun remove(id: Long) {}
    override fun updateQuantity(id: Long, quantity: Int) {}

    override fun clear() {
        _items.clear()
    }
}