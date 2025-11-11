package com.yuuuja.coffeeorderapp.model

enum class Category { COFFEE, NON_COFFEE, TEA, ADE }
enum class Temperature { HOT, ICE }

data class TemperatureRule(
    val allowHot: Boolean,
    val allowIce: Boolean
)

data class MenuMini(
    val id: Long,
    val category: Category,
    val name: String,
    val price: Int,
    val rule: TemperatureRule
)

// 더미 메뉴 데이터 (임시)
val dummyMenus = listOf(
    // 커피
    MenuMini(1, Category.COFFEE, "아메리카노", 2000, TemperatureRule(true, true)),
    MenuMini(2, Category.COFFEE, "카페라떼", 3200, TemperatureRule(true, true)),
    MenuMini(3, Category.COFFEE, "카페모카", 3700, TemperatureRule(true, true)),
    MenuMini(4, Category.COFFEE, "바닐라라떼", 3700, TemperatureRule(true, true)),
    MenuMini(5, Category.COFFEE, "카라멜마끼아또", 3700, TemperatureRule(true, true)),

    // 논커피
    MenuMini(101, Category.NON_COFFEE, "초코라떼", 4000, TemperatureRule(true, true)),
    MenuMini(102, Category.NON_COFFEE, "녹차라떼", 4000, TemperatureRule(true, true)),
    MenuMini(103, Category.NON_COFFEE, "딸기라떼", 5000, TemperatureRule(false, true)), // ICE ONLY
    MenuMini(104, Category.NON_COFFEE, "밀크티", 4000, TemperatureRule(true, true)),
    MenuMini(105, Category.NON_COFFEE, "아이스티", 3500, TemperatureRule(false, true)), // ICE ONLY

    // 티
    MenuMini(201, Category.TEA, "얼그레이", 3800, TemperatureRule(true, true)),
    MenuMini(202, Category.TEA, "페퍼민트", 3800, TemperatureRule(true, true)),
    MenuMini(203, Category.TEA, "루이보스", 3800, TemperatureRule(true, true)),
    MenuMini(204, Category.TEA, "캐모마일", 3800, TemperatureRule(true, true)),

    // 에이드 (ICE ONLY)
    MenuMini(301, Category.ADE, "레몬에이드", 4500, TemperatureRule(false, true)),
    MenuMini(302, Category.ADE, "자몽에이드", 4500, TemperatureRule(false, true)),
    MenuMini(303, Category.ADE, "청포도에이드", 4500, TemperatureRule(false, true)),
    MenuMini(304, Category.ADE, "오미자에이드", 5800, TemperatureRule(false, true)),
    MenuMini(305, Category.ADE, "유자에이드", 4800, TemperatureRule(false, true)),
)
