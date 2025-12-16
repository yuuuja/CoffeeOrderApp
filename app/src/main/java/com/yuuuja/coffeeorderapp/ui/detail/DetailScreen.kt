package com.yuuuja.coffeeorderapp.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun DetailScreen(navController: NavController, id: Long) {
    Scaffold(
        //topBar = { TopAppBar(title = { Text("상세 #$id") }) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Button(onClick = { /* TODO: 장바구니 담기 연결 예정 */ },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("장바구니 담기") }

                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { navController.navigate("cart") },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("장바구니 보기") }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(20.dp)) {
            Text("여기에 옵션(핫/아이스, 사이즈) UI가 들어갈 자리",
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(12.dp))
            Text(
                "지금은 스켈레톤 상태입니다.",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
