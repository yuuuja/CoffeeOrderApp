package com.yuuuja.coffeeorderapp.model

import com.yuuuja.coffeeorderapp.rules.ShotOption

data class CartItem(
    val id: Long,
    val menu: MenuMini,
    val quantity: Int,
    val cup: CupType,
    val temp: Temperature,
    val size: DrinkSize,
    val shot: ShotOption
)

//옵션 추가금 계산
fun CartItem.unitExtra(): Int{
    val sizeExtra = when (menu.category){
        Category.COFFEE, Category.NON_COFFEE -> when (size) {
            DrinkSize.S -> -400
            DrinkSize.M -> 0
            DrinkSize.L -> 1200
        }
        Category.TEA -> 0
        Category.ADE -> when(size) {
            DrinkSize.M -> 0
            DrinkSize.L -> 1200
            else -> 0
        }
    }
    return sizeExtra + shot.extra
}

// 단가
fun CartItem.unitPrice(): Int = menu.price + unitExtra()

// 수량 포함 가격
fun CartItem.linePrice(): Int = unitPrice() * quantity