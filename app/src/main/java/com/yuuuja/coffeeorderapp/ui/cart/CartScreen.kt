package com.yuuuja.coffeeorderapp.ui.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yuuuja.coffeeorderapp.model.CartItem
import com.yuuuja.coffeeorderapp.model.CupType
import com.yuuuja.coffeeorderapp.model.linePrice
import com.yuuuja.coffeeorderapp.model.optionDescription
import com.yuuuja.coffeeorderapp.model.unitPrice
import com.yuuuja.coffeeorderapp.ui.common.SmallSquareButton
import com.yuuuja.coffeeorderapp.ui.detail.DetailScreen
import com.yuuuja.coffeeorderapp.ui.theme.Grey
import com.yuuuja.coffeeorderapp.ui.theme.Kaki
import com.yuuuja.coffeeorderapp.ui.theme.LightBeige
import com.yuuuja.coffeeorderapp.ui.theme.LightBrown
import com.yuuuja.coffeeorderapp.ui.theme.LightGrey
import com.yuuuja.coffeeorderapp.util.won
import com.yuuuja.coffeeorderapp.utils.imageResOf
import com.yuuuja.coffeeorderapp.viewmodel.CartContract
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel
import com.yuuuja.coffeeorderapp.ui.preview.FakeCartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cart: CartContract) {

    var items = cart.items
    val totalPrice = items.sumOf { it.linePrice() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("home", false) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Kaki)
                    }
                },
                title = { Text("장바구니") }
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                CartBottomBar(
                    totalPrice = totalPrice,
                    onOrder = { /* ToDo */ }
                )
            }
        }
    ) { pad ->
        if (items.isEmpty()) {
            EmptyCart(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                onGoBack = { navController.popBackStack("home", false) }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items, key = { it.id }
                ) { item ->
                    CartItemRow(
                        item = item,
                        onRemove = { cart.remove(item.id) },
                        onMinus = { cart.updateQuantity(item.id, item.quantity - 1) },
                        onPlus = { cart.updateQuantity(item.id, item.quantity + 1) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun EmptyCart(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "장바구니가 비었습니다",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onGoBack,
                border = BorderStroke(1.dp, Kaki),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Kaki)
            ) { Text("돌아가기") }
        }
    }
}

@Composable
fun CartBottomBar(
    totalPrice: Int,
    onOrder: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("총 금액", style = MaterialTheme.typography.titleMedium, color = Grey)
                Spacer(Modifier.width(8.dp))
                Text(totalPrice.won(), style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = onOrder,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Kaki,
                    contentColor = Color.White
                )
            ) {
                Text("주문하기", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onRemove: () -> Unit,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
                .heightIn(min = 95.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(imageResOf(item.menu)),
                contentDescription = item.menu.name,
                modifier = Modifier
                    .size(95.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(15.dp))

            Column(Modifier.weight(1f)) {
                Text(item.menu.name, style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "${item.cup.label}  \n${item.temp.name}    ${item.size.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Grey
                )
                item.optionDescription()?.let { optionText ->
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Grey
                    )
                }

                Spacer(Modifier.height(5.dp))

                Text(
                    text = item.linePrice().won(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Close,
                        contentDescription = "삭제",
                        tint = Kaki,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.height(40.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallSquareButton(text = "－", onClick = onMinus)
                    Spacer(Modifier.width(10.dp))
                    Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                    Spacer(Modifier.width(10.dp))
                    SmallSquareButton(text = "＋", onClick = onPlus)
                }
            }
        }
    }
}



private val CupType.label: String
    get() = when (this) {
        CupType.DISPOSABLE -> "일회용컵"
        CupType.PERSONAL -> "개인컵"
    }