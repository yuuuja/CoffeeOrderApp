package com.yuuuja.coffeeorderapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuuuja.coffeeorderapp.model.Category
import com.yuuuja.coffeeorderapp.model.MenuMini
import com.yuuuja.coffeeorderapp.model.dummyMenus
import com.yuuuja.coffeeorderapp.ui.common.AppChip
import com.yuuuja.coffeeorderapp.ui.common.ChipSpecs
import com.yuuuja.coffeeorderapp.ui.theme.*
import com.yuuuja.coffeeorderapp.util.won
import com.yuuuja.coffeeorderapp.utils.imageResOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val categories = listOf(
        "커피" to Category.COFFEE,
        "논커피" to Category.NON_COFFEE,
        "티" to Category.TEA,
        "에이드" to Category.ADE
    )

    var selected by remember { mutableStateOf(0) }
    val selectedCategory = categories[selected].second

    val filtered = remember(selectedCategory) {
        dummyMenus.filter { it.category == selectedCategory }
    }

    val listState = rememberLazyListState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MENU") },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "장바구니", tint = Kaki)
                    }
                }
            )
        },

        ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .fillMaxSize(),
        ) {
            // 카테고리 탭
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (label, _) ->
                    AppChip(
                        label = label,
                        selected = (label == categories[selected].first),
                        onClick = { selected = categories.indexOfFirst { it.first == label } },
                        spec = ChipSpecs.Medium
                    )
                }
            }

            Spacer(Modifier.height(15.dp))

            //메뉴 리스트
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered.size) { i ->
                    val m = filtered[i]
                    MenuRow(
                        menu = m,
                        onClick = { navController.navigate("detail/${m.id}") }
                    )
                }
            }
        }
    }
}


@Composable
private fun MenuRow(
    menu: MenuMini,
    onClick: () -> Unit,
) {
    val imgRes = imageResOf(menu)
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 메뉴 이미지
            Image(
                painter = painterResource(imgRes),
                contentDescription = menu.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    menu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (menu.rule.allowHot && menu.rule.allowIce) {
                        AssistChip(
                            onClick = {},
                            label = { Text("ICE", color = Blue) },
                            enabled = false
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text("HOT", color = Red) },
                            enabled = false
                        )
                    }
                    if (!menu.rule.allowHot && menu.rule.allowIce)
                        AssistChip(
                            onClick = {},
                            label = { Text("ICE ONLY", color = Blue) },
                            enabled = false
                        )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    menu.price.won(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 17.sp,
                    color = Grey
                )
            }

        }
    }
}
