package com.yuuuja.coffeeorderapp.rules

import com.yuuuja.coffeeorderapp.model.Category
import com.yuuuja.coffeeorderapp.model.TemperatureRule

fun hasCupOption(category: Category): Boolean = true

fun hasPersonalOption(category: Category): Boolean =
    category == Category.COFFEE

data class TempConfig(
    val enableIce: Boolean,
    val enableHot: Boolean
)

fun tempConfigOf(category: Category, rule: TemperatureRule): TempConfig =
    when (category){
        Category.ADE -> TempConfig(true, false)
        else -> TempConfig(rule.allowIce, rule.allowHot)
    }

data class SizeConfig(
    val showChips: Boolean,
    val enableSmall: Boolean,
    val enableMedium: Boolean,
    val enableLarge: Boolean,
    val showDefaultLabel: Boolean = false
)

fun sizeConfigOf(category: Category): SizeConfig =
    when(category) {
        Category.COFFEE,
        Category.NON_COFFEE -> SizeConfig(
            showChips = true,
            enableSmall = true, enableMedium = true, enableLarge = true
        )
        Category.TEA -> SizeConfig(
            showChips = false,
            enableSmall = false, enableMedium = false, enableLarge = false,
            showDefaultLabel = true
        )
        Category.ADE -> SizeConfig(
            showChips = true,
            enableSmall = false, enableMedium = true, enableLarge = true
        )
    }

