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
import com.yuuuja.coffeeorderapp.model.unitPrice
import com.yuuuja.coffeeorderapp.ui.detail.DetailScreen
import com.yuuuja.coffeeorderapp.ui.theme.Grey
import com.yuuuja.coffeeorderapp.ui.theme.Kaki
import com.yuuuja.coffeeorderapp.ui.theme.LightGrey
import com.yuuuja.coffeeorderapp.util.won
import com.yuuuja.coffeeorderapp.utils.imageResOf
import com.yuuuja.coffeeorderapp.viewmodel.CartContract
import com.yuuuja.coffeeorderapp.viewmodel.CartViewModel
import com.yuuuja.coffeeorderapp.viewmodel.FakeCartViewModel

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

@Preview(showBackground = true)
@Composable
fun preview() {
    val navController = rememberNavController()
    val fakeCart = FakeCartViewModel()
    CartScreen(navController = navController, cart = fakeCart)
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
                Text("주문하기")
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
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(imageResOf(item.menu)),
                contentDescription = item.menu.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.weight(1f))

            Column(Modifier.weight(1f)) {
                Text(item.menu.name, style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${item.cup.label}  ·  ${item.temp.name}  ·  ${item.size.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Grey
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = item.unitPrice().won(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, contentDescription = "삭제", tint = Grey)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    SmallSquareButton(text = "－", onClick = onMinus)
                    Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                    SmallSquareButton(text = "＋", onClick = onPlus)
                }
            }
        }
    }
}

@Composable
private fun SmallSquareButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(30.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGrey,
            contentColor = Color.White
        )
    ) { Text(text) }
}

private val CupType.label: String
    get() = when (this) {
        CupType.DISPOSABLE -> "일회용컵"
        CupType.PERSONAL -> "개인컵"
    }