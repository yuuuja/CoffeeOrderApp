package com.yuuuja.coffeeorderapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.model.Category
import com.yuuuja.coffeeorderapp.model.CupType
import com.yuuuja.coffeeorderapp.model.CupType.*
import com.yuuuja.coffeeorderapp.model.DrinkSize
import com.yuuuja.coffeeorderapp.model.MenuMini
import com.yuuuja.coffeeorderapp.model.Temperature
import com.yuuuja.coffeeorderapp.model.dummyMenus
import com.yuuuja.coffeeorderapp.rules.hasCupOption
import com.yuuuja.coffeeorderapp.rules.hasPersonalOption
import com.yuuuja.coffeeorderapp.rules.sizeConfigOf
import com.yuuuja.coffeeorderapp.rules.tempConfigOf
import com.yuuuja.coffeeorderapp.ui.common.AppChip
import com.yuuuja.coffeeorderapp.ui.common.ChipSpecs
import com.yuuuja.coffeeorderapp.ui.theme.DarkBrown
import com.yuuuja.coffeeorderapp.ui.theme.Kaki
import com.yuuuja.coffeeorderapp.ui.theme.LightGrey
import com.yuuuja.coffeeorderapp.utils.imageResOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Long) {

    val menu: MenuMini = remember(id) {
        dummyMenus.first { it.id == id }
    }

    // 수량 / 사이즈 / 온도 등은 나중에 ViewModel로 빼도 되고, 지금은 remember로 충분
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
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
                totalPrice = menu.price,
                quantity = 1,
                onMinus = { /* TODO */ },
                onPlus = { /* TODO */ },
                onBuyNow = { /* TODO */ },
                onAddToCart = { navController.navigate("cart") }
            )
        }
    ) { pad ->
        DetailContent(
            menu = menu,
            modifier = Modifier
                .padding(pad)
                .padding(horizontal = 40.dp, vertical = 16.dp)
        )
    }
}

@Composable
fun DetailContent(
    menu: MenuMini,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
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
        Spacer(Modifier.height(4.dp))
        Text(
            text = "간단한 메뉴 설명",
            style = MaterialTheme.typography.bodySmall,
            color = DarkBrown,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        OptionSection(menu = menu)
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
fun OptionSection(menu: MenuMini) {
    val category: Category = menu.category
    val rule = menu.rule

    // 상태
    var cup by remember { mutableStateOf(DISPOSABLE) }
    var temp by remember { mutableStateOf(Temperature.ICE) }
    var size by remember { mutableStateOf(DrinkSize.M) }

    // 규칙 적용
    val tempCfg = tempConfigOf(category, rule)
    val sizeCfg = sizeConfigOf(category)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if(hasCupOption(category)){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("컵 선택", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(5.dp))
                Text(
                    "선택필수",
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkBrown
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppChip("일회용 컵",
                    selected = cup == CupType.DISPOSABLE,
                    onClick = { cup = CupType.DISPOSABLE },
                    spec = ChipSpecs.Medium
                )
                AppChip("개인 컵",
                    selected = cup == CupType.PERSONAL,
                    onClick = { cup = CupType.PERSONAL },
                    spec = ChipSpecs.Medium
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text("ICE & HOT", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(5.dp))
            Text(
                "선택필수",
                style = MaterialTheme.typography.labelMedium,
                color = DarkBrown
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AppChip("ICE",
                selected = temp == Temperature.ICE,
                onClick = { temp = Temperature.ICE },
                spec = ChipSpecs.Medium,
                enabled = tempCfg.enableIce
            )
            AppChip("HOT",
                selected = temp == Temperature.HOT,
                onClick = { temp = Temperature.HOT },
                spec = ChipSpecs.Medium,
                enabled = tempCfg.enableHot
            )
        }

        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("SIZE", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(5.dp))
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
                    onClick = { size = DrinkSize.M },
                    spec = ChipSpecs.Small
                )
            }
            sizeCfg.showChips -> {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (sizeCfg.enableSmall) {
                        AppChip(
                            label = "S\n(-400)",
                            selected = size == DrinkSize.S,
                            onClick = { size = DrinkSize.S },
                            spec = ChipSpecs.Small
                        )
                    }
                    if (sizeCfg.enableMedium) {
                        AppChip(
                            label = "M\n(+0)",
                            selected = size == DrinkSize.M,
                            onClick = { size = DrinkSize.M },
                            spec = ChipSpecs.Small
                        )
                    }
                    if (sizeCfg.enableLarge) {
                        AppChip(
                            label = "L\n(+1,200)",
                            selected = size == DrinkSize.L,
                            onClick = { size = DrinkSize.L },
                            spec = ChipSpecs.Small
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if(hasPersonalOption(category)){
            Text("퍼스널 옵션", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppChip(
                    label = "연하게\n(+0)",
                    selected = false,
                    onClick = { /* TODO */ },
                    spec = ChipSpecs.Small
                )
                AppChip(
                    label = "+1샷\n(+600)",
                    selected = false,
                    onClick = { /* TODO */ },
                    spec = ChipSpecs.Small
                )
                AppChip(
                    label = "+2샷\n(+1,200)",
                    selected = false,
                    onClick = { /* TODO */ },
                    spec = ChipSpecs.Small
                )
            }
        }
        // ... 샷 추가, 연하게 등
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
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 여기 나중에 "총 금액 / 수량" 영역 넣고

        Button(
            onClick = onBuyNow,
            modifier = Modifier.fillMaxWidth()

        ) { Text("바로 구매") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onAddToCart,
            modifier = Modifier.fillMaxWidth()
        ) { Text("장바구니 담기") }
    }
}
