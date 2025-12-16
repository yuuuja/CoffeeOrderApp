// utils/MenuImages.kt
package com.yuuuja.coffeeorderapp.utils

import androidx.annotation.DrawableRes
import com.yuuuja.coffeeorderapp.R
import com.yuuuja.coffeeorderapp.model.MenuMini

@DrawableRes
fun imageResOf(menu: MenuMini): Int = when (menu.name) {
    "아메리카노" -> R.drawable.americano
    "카페라떼" -> R.drawable.cafe_latte
    "카페모카" -> R.drawable.cafe_mocha
    "바닐라라떼" -> R.drawable.vanilla_latte
    "카라멜마끼아또" -> R.drawable.caramel_macchiato
    "초코라떼" -> R.drawable.choco_latte
    "녹차라떼" -> R.drawable.greentea_latte
    "딸기라떼" -> R.drawable.strawberry_latte
    "밀크티" -> R.drawable.milk_tea
    "아이스티" -> R.drawable.icetea
    "얼그레이" -> R.drawable.earlgrey
    "페퍼민트" -> R.drawable.peppermint
    "루이보스" -> R.drawable.rooibos
    "캐모마일" -> R.drawable.chamomile
    "레몬에이드" -> R.drawable.lemon_ade
    "자몽에이드" -> R.drawable.grapefruit_ade
    "청포도에이드" -> R.drawable.greengrape_ade
    "오미자에이드" -> R.drawable.omija_ade
    "유자에이드" -> R.drawable.yuja_ade
    else -> error("알 수 없는 메뉴: ${menu.name}")
}
