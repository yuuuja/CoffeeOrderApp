package com.yuuuja.coffeeorderapp.ui.orderInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yuuuja.coffeeorderapp.model.CartItem
import com.yuuuja.coffeeorderapp.model.CupType
import com.yuuuja.coffeeorderapp.model.linePrice
import com.yuuuja.coffeeorderapp.model.optionDescription
import com.yuuuja.coffeeorderapp.ui.preview.FakeCartViewModel
import com.yuuuja.coffeeorderapp.ui.theme.Grey
import com.yuuuja.coffeeorderapp.ui.theme.Kaki
import com.yuuuja.coffeeorderapp.ui.theme.LightBrown
import com.yuuuja.coffeeorderapp.ui.theme.LightGrey
import com.yuuuja.coffeeorderapp.util.won
import com.yuuuja.coffeeorderapp.utils.imageResOf
import com.yuuuja.coffeeorderapp.viewmodel.CartContract

private val CupType.label: String
    get() = when (this) {
        CupType.DISPOSABLE -> "일회용컵"
        CupType.PERSONAL -> "개인컵"
    }

enum class OrderEntryType {
    FROM_DETAIL,
    FROM_CART
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderInfoScreen(navController: NavController, cart: CartContract, entryType: OrderEntryType) {

    var requestMessage by remember { mutableStateOf("") }
    var rememberRequest by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<PaymentMethod?>(null) }
    var agreed by remember { mutableStateOf(false) }

    val totalPrice = cart.items.sumOf { it.linePrice() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        when (entryType) {
                            OrderEntryType.FROM_DETAIL -> navController.popBackStack("home", false)
                            OrderEntryType.FROM_CART -> navController.popBackStack("cart", false)
                        }
                    }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Kaki)
                    }
                },
                title = { Text("주문 / 결제") }
            )
        },
        bottomBar = {
            OrderBottomBar(
                totalPrice = totalPrice,
                enabled = selectedPayment != null && agreed,
                onOrder = { /* ToDo */ }
            )
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("주문 내역")
                }
            }

            items(cart.items) { item ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OrderItemRow(item)
                }
            }

            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 5.dp,
                    color = LightGrey
                )
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("요청 사항")

                    Spacer(Modifier.height(10.dp))

                    val maxLength = 20

                    OutlinedTextField(
                        value = requestMessage,
                        onValueChange = {
                            if (it.length <= maxLength) {
                                requestMessage = it
                            }
                        },
                        placeholder = { Text("요청 사항이 있으면 적어주세요(최대 20자)", color = Grey) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.height(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberRequest,
                            onCheckedChange = { rememberRequest = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = LightBrown,
                                uncheckedColor = Grey
                            )
                        )
                        Text("다음에도 사용")
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("쿠폰")

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = "사용 가능한 쿠폰이 없습니다.",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        //enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Grey
                            )
                        }
                    )
                }

            }

            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 5.dp,
                    color = LightGrey
                )
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("결제 수단")

                    Spacer(Modifier.height(10.dp))

                    PaymentMethodGrid(
                        selected = selectedPayment,
                        onSelect = { selectedPayment = it }
                    )
                }
            }

            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 5.dp,
                    color = LightGrey
                )
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "총 금액 ",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("${totalPrice.won()}", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = LightGrey
                )
            }

            //약관 동의
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreed,
                        onCheckedChange = { agreed = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = LightBrown,
                            uncheckedColor = Grey
                        )
                    )
                    Text(
                        text = "주문상품정보 및 결제대행 서비스 이용약관에 모두 동의합니다.",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    val navController = rememberNavController()
    val fakeCart = FakeCartViewModel()
    OrderInfoScreen(
        navController = navController,
        entryType = OrderEntryType.FROM_CART,
        cart = fakeCart
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun OrderItemRow(item: CartItem) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .heightIn(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(imageResOf(item.menu)),
                contentDescription = item.menu.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(item.menu.name, style = MaterialTheme.typography.titleMedium)
                Text("${item.cup.label} / ${item.temp.name} / ${item.size.name}", color = Grey)

                item.optionDescription()?.let {
                    Text(it, color = Grey)
                }
                Spacer(Modifier.height(4.dp))
                Text("${item.linePrice().won()}(수량 ${item.quantity}잔)")

            }
        }
    }
}

@Composable
fun OrderBottomBar(
    totalPrice: Int,
    enabled: Boolean,
    onOrder: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Button(
                onClick = onOrder,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Kaki)
            ) {
                Text("결제하기")
            }
        }
    }
}