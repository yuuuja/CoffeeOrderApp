package com.yuuuja.coffeeorderapp.ui.detail

import com.yuuuja.coffeeorderapp.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yuuuja.coffeeorderapp.model.CartItem
import com.yuuuja.coffeeorderapp.model.Category
import com.yuuuja.coffeeorderapp.model.CupType
import com.yuuuja.coffeeorderapp.model.DrinkSize
import com.yuuuja.coffeeorderapp.model.MenuMini
import com.yuuuja.coffeeorderapp.model.Temperature
import com.yuuuja.coffeeorderapp.model.dummyMenus
import com.yuuuja.coffeeorderapp.rules.ShotOption
import com.yuuuja.coffeeorderapp.rules.hasCupOption
import com.yuuuja.coffeeorderapp.rules.hasPersonalOption
import com.yuuuja.coffeeorderapp.rules.sizeConfigOf
import com.yuuuja.coffeeorderapp.rules.tempConfigOf
import com.yuuuja.coffeeorderapp.ui.common.AppChip
import com.yuuuja.coffeeorderapp.ui.common.ChipSpecs
import com.yuuuja.coffeeorderapp.ui.theme.DarkBrown
import com.yuuuja.coffeeorderapp.ui.theme.Grey
import com.yuuuja.coffeeorderapp.ui.theme.Kaki
import com.yuuuja.coffeeorderapp.ui.theme.LightBrown
import com.yuuuja.coffeeorderapp.util.won
import com.yuuuja.coffeeorderapp.utils.imageResOf
import com.yuuuja.coffeeorderapp.viewmodel.CartContract
import com.yuuuja.coffeeorderapp.ui.preview.FakeCartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Long, cartContract: CartContract) {

    val menu: MenuMini = remember(id) {
        dummyMenus.first { it.id == id }
    }

    // 수량 / 사이즈 / 온도 등은 나중에 ViewModel로 빼도 되고, 지금은 remember로 충분
    var quantity by remember { mutableStateOf(1) }
    var optionExtra by remember { mutableStateOf(0) }
    var cup by remember { mutableStateOf(CupType.DISPOSABLE) }
    var temp by remember { mutableStateOf(Temperature.ICE) }
    var size by remember { mutableStateOf(DrinkSize.M) }
    var shot by remember { mutableStateOf(ShotOption.NONE) }

    val basePrice = menu.price

    // 총 금액 = (기본가+옵션추가금) * 수량
    val totalPrice = remember(basePrice, optionExtra, quantity) {
        (basePrice + optionExtra) * quantity
    }

    var showCartDialog by remember { mutableStateOf(false) }



    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Kaki)
                    }
                },
                title = { Text("") },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "장바구니", tint = Kaki)
                    }
                }
            )
        },
        bottomBar = {
            DetailBottomBar(
                totalPrice = totalPrice,
                quantity = quantity,
                onMinus = { if (quantity > 1) quantity-- },
                onPlus = { quantity++ },
                onBuyNow = { /* TODO */ },
                onAddToCart = {
                    cartContract.add(
                        CartItem(
                            id = System.currentTimeMillis(), // 임시 ID (나중에 UUID/증가값으로 개선)
                            menu = menu,
                            quantity = quantity,
                            cup = cup,
                            temp = temp,
                            size = size,
                            shot = shot
                        )
                    )
                    showCartDialog = true
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxWidth()
        ) {
            DetailContent(
                menu = menu,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                OptionSection(
                    menu = menu,
                    cup = cup, onCupChange = { cup = it },
                    temp = temp, onTempChange = { temp = it },
                    size = size, onSizeChange = { size = it },
                    shot = shot, onShotChange = { shot = it },
                    onExtraPriceChange = { extra -> optionExtra = extra }
                )
            }

            if (showCartDialog) {
                CartAddedDialog(
                    onMoveToCart = {
                        showCartDialog = false
                        navController.navigate("cart")
                    },
                    onContinue = {
                        showCartDialog = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    val navController = rememberNavController()
    val fakeCart = FakeCartViewModel()
    DetailScreen(navController = navController, id = 1, cartContract = fakeCart)
}

@Composable
fun DetailContent(
    menu: MenuMini,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DetailImage(menu = menu)

        Spacer(Modifier.height(12.dp))

        // 메뉴이름 + 설명
        Text(
            text = menu.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
//        Spacer(Modifier.height(4.dp))
//        Text(
//            text = "간단한 메뉴 설명",
//            style = MaterialTheme.typography.bodySmall,
//            color = DarkBrown,
//            textAlign = TextAlign.Center
//        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun DetailImage(menu: MenuMini) {
    val imgRes = imageResOf(menu)
    Image(
        painter = painterResource(imgRes),
        contentDescription = menu.name,
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop

    )
}

@Composable
fun OptionSection(
    menu: MenuMini,
    cup: CupType,
    onCupChange: (CupType) -> Unit,
    temp: Temperature,
    onTempChange: (Temperature) -> Unit,
    size: DrinkSize,
    onSizeChange: (DrinkSize) -> Unit,
    shot: ShotOption,
    onShotChange: (ShotOption) -> Unit,
    onExtraPriceChange: (Int) -> Unit
) {
    val category: Category = menu.category
    val rule = menu.rule


    // 규칙 적용
    val tempCfg = tempConfigOf(category, rule)
    val isIceOnly = tempCfg.enableIce && !tempCfg.enableHot
    val sizeCfg = sizeConfigOf(category)

    val sizeExtra = remember(category, size) {
        when (category) {
            Category.COFFEE, Category.NON_COFFEE -> when (size) {
                DrinkSize.S -> -400
                DrinkSize.M -> 0
                DrinkSize.L -> 1200
            }

            Category.TEA -> 0
            Category.ADE -> when (size) {
                DrinkSize.M -> 0
                DrinkSize.L -> 1200
                else -> 0
            }
        }
    }

    val shotExtra = shot.extra

    LaunchedEffect(size, shot) {
        val extra = sizeExtra + shotExtra
        onExtraPriceChange(extra)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        if (hasCupOption(category)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("컵 선택", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(10.dp))
                Text(
                    "선택필수",
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkBrown
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppChip(
                    "일회용 컵",
                    selected = cup == CupType.DISPOSABLE,
                    onClick = { onCupChange(CupType.DISPOSABLE) },
                    spec = ChipSpecs.Medium
                )
                AppChip(
                    "개인 컵",
                    selected = cup == CupType.PERSONAL,
                    onClick = { onCupChange(CupType.PERSONAL) },
                    spec = ChipSpecs.Medium
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("ICE & HOT", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(10.dp))
            Text(
                "선택필수",
                style = MaterialTheme.typography.labelMedium,
                color = DarkBrown
            )
        }

        Spacer(Modifier.height(8.dp))

        if (isIceOnly) {
            AppChip(
                label = "ICE ONLY",
                selected = true,
                onClick = {},
                spec = ChipSpecs.Medium,
                enabled = false, //클릭 안되게 고정
            )
        } else {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppChip(
                    "ICE",
                    selected = temp == Temperature.ICE,
                    onClick = { onTempChange(Temperature.ICE) },
                    spec = ChipSpecs.Medium,
                    enabled = tempCfg.enableIce
                )
                AppChip(
                    "HOT",
                    selected = temp == Temperature.HOT,
                    onClick = { onTempChange(Temperature.HOT) },
                    spec = ChipSpecs.Medium,
                    enabled = tempCfg.enableHot
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SIZE", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(10.dp))
            Text(
                "선택필수",
                style = MaterialTheme.typography.labelMedium,
                color = DarkBrown
            )
        }

        Spacer(Modifier.height(8.dp))

        when {
            sizeCfg.showDefaultLabel -> {
                AppChip(
                    label = "M",
                    selected = size == DrinkSize.M,
                    onClick = { onSizeChange(DrinkSize.M) },
                    spec = ChipSpecs.Small
                )
            }

            sizeCfg.showChips -> {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (sizeCfg.enableSmall) {
                        AppChip(
                            label = "S\n(-400)",
                            selected = size == DrinkSize.S,
                            onClick = { onSizeChange(DrinkSize.S) },
                            spec = ChipSpecs.Small
                        )
                    }
                    if (sizeCfg.enableMedium) {
                        AppChip(
                            label = "M\n(+0)",
                            selected = size == DrinkSize.M,
                            onClick = { onSizeChange(DrinkSize.M) },
                            spec = ChipSpecs.Small
                        )
                    }
                    if (sizeCfg.enableLarge) {
                        AppChip(
                            label = "L\n(+1,200)",
                            selected = size == DrinkSize.L,
                            onClick = { onSizeChange(DrinkSize.L) },
                            spec = ChipSpecs.Small
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (hasPersonalOption(category)) {

            Text("퍼스널 옵션", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text("샷 추가", style = MaterialTheme.typography.labelLarge, color = Grey)

            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppChip(
                    label = "연하게\n(+0)",
                    selected = (shot == ShotOption.LIGHT),
                    onClick = {
                        onShotChange(if (shot == ShotOption.LIGHT) ShotOption.NONE else ShotOption.LIGHT)
                    },
                    spec = ChipSpecs.Small
                )
                AppChip(
                    label = "+1샷\n(+600)",
                    selected = (shot == ShotOption.PLUS1SHOT),
                    onClick = {
                        onShotChange(if (shot == ShotOption.PLUS1SHOT) ShotOption.NONE else ShotOption.PLUS1SHOT)
                    },
                    spec = ChipSpecs.Small
                )
                AppChip(
                    label = "+2샷\n(+1,200)",
                    selected = (shot == ShotOption.PLUS2SHOT),
                    onClick = {
                        onShotChange(if (shot == ShotOption.PLUS2SHOT) ShotOption.NONE else ShotOption.PLUS2SHOT)
                    },
                    spec = ChipSpecs.Small
                )
            }
        }
    }
}

@Composable
fun DetailBottomBar(
    totalPrice: Int,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    onBuyNow: () -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        color = Color.White,
        modifier = Modifier.heightIn(max = 140.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp)
        ) {
            // 총 금액 / 수량
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(100.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(" 총 금액", style = MaterialTheme.typography.titleMedium, color = Grey)
                    Spacer(Modifier.width(15.dp))
                    Text(text = totalPrice.won(), style = MaterialTheme.typography.titleMedium)

                    //Spacer(Modifier.width(150.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onMinus,
                        modifier = Modifier.size(28.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBrown,
                            contentColor = Color.White
                        ),

                        ) {
                        Text("-")
                    }
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = quantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.width(5.dp))
                    Button(
                        onClick = onPlus,
                        modifier = Modifier.size(28.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBrown,
                            contentColor = Color.White
                        ),

                        ) {
                        Text("+")
                    }
                }
            }


            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBuyNow,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Kaki,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) { Text("바로 구매") }

                OutlinedButton(
                    onClick = onAddToCart,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Kaki
                    ),
                    border = BorderStroke(1.dp, Kaki),
                    shape = RoundedCornerShape(18.dp)
                ) { Text("장바구니 담기") }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CartDialogPreview() {
//    CartAddedDialog(
//        onMoveToCart = {},
//        onContinue = {}
//    )
//}


@Composable
fun CartAddedDialog(
    onMoveToCart: () -> Unit,
    onContinue: () -> Unit
) {
    Dialog(onDismissRequest = onContinue) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(2.dp, DarkBrown)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.symbol),
                    contentDescription = "장바구니에 담겼습니다",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "장바구니에 상품이 정상적으로 담겼습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onMoveToCart,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Kaki
                        ),
                        border = BorderStroke(1.dp, Kaki),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("장바구니 이동")
                    }

                    OutlinedButton(
                        onClick = onContinue,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Kaki
                        ),
                        border = BorderStroke(1.dp, Kaki),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("계속 담기")
                    }
                }
            }
        }
    }
}