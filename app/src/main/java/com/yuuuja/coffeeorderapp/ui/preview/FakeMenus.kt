package com.yuuuja.coffeeorderapp.ui.preview

import com.yuuuja.coffeeorderapp.R
import com.yuuuja.coffeeorderapp.model.Category
import com.yuuuja.coffeeorderapp.model.MenuMini
import com.yuuuja.coffeeorderapp.model.TemperatureRule

object FakeMenus {
    val americano = MenuMini(
        id = 1L,
        category = Category.COFFEE,
        name = "아메리카노",
        price = 2000,
        rule = TemperatureRule(true, true)
    )

    val strawberryLatte = MenuMini(
        2L,
        Category.NON_COFFEE,
        "딸기라떼",
        5000,
        TemperatureRule(false, true)
    )


}