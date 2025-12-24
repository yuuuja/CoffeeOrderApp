package com.yuuuja.coffeeorderapp.ui.orderInfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import com.yuuuja.coffeeorderapp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuuuja.coffeeorderapp.ui.theme.Kaki
import com.yuuuja.coffeeorderapp.ui.theme.LightGrey

enum class PaymentMethod (val label: String){
    CARD("신용/체크 카드"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이"),
    TOSS_PAY("토스페이"),
    BANK("무통장 입금"),
    MOBILE("휴대폰 결제")
}

@Composable
fun PaymentMethodGrid(
    selected: PaymentMethod?,
    onSelect: (PaymentMethod) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PaymentRow(
            methods = listOf(
                PaymentMethod.CARD,
                PaymentMethod.KAKAO_PAY
            ),
            selected = selected,
            onSelect = onSelect
        )

        PaymentRow(
            methods = listOf(
                PaymentMethod.NAVER_PAY,
                PaymentMethod.TOSS_PAY
            ),
            selected = selected,
            onSelect = onSelect
        )

        PaymentRow(
            methods = listOf(
                PaymentMethod.BANK,
                PaymentMethod.MOBILE
            ),
            selected = selected,
            onSelect = onSelect
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodGridPreview() {
    var selected by remember { mutableStateOf<PaymentMethod?>(PaymentMethod.TOSS_PAY) }
    Column {
        Text("결제 수단", modifier = Modifier.padding(16.dp))
        Spacer(Modifier.height(10.dp))
        PaymentMethodGrid(
            selected = selected,
            onSelect = { selected = it }
        )
        Spacer(Modifier.height(10.dp))
    }
}


@Composable
private fun PaymentRow(
    methods: List<PaymentMethod>,
    selected: PaymentMethod?,
    onSelect: (PaymentMethod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        methods.forEach { method ->
            PaymentButton(
                method = method,
                selected = method == selected,
                onClick = { onSelect(method) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PaymentButton(
    method: PaymentMethod,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(55.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) Kaki else Color.White,
            contentColor = if (selected) Color.White else Color.Black
        ),
        border = if (selected) null else BorderStroke(1.dp, LightGrey)
    ) {
        when (method) {
            PaymentMethod.TOSS_PAY -> {
                Image(
                    painter = painterResource(
                        id = if (selected)
                            R.drawable.tosspay_logo_white
                        else
                            R.drawable.tosspay_logo_black
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    contentScale = ContentScale.Fit
                )
            }
            PaymentMethod.KAKAO_PAY -> {
                Image(
                    painter = painterResource(R.drawable.kakaopay_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentScale = ContentScale.Fit
                )
            }

            PaymentMethod.NAVER_PAY -> {
                Image(
                    painter = painterResource(R.drawable.npay_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentScale = ContentScale.Fit
                )
            }

            else -> {
                Text(method.label, style = MaterialTheme.typography.bodyMedium)
            }

        }

    }
}