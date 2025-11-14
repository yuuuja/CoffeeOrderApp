package com.yuuuja.coffeeorderapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuuuja.coffeeorderapp.ui.theme.Kaki


// 색 팔레트(공통)
data class ChipPalette(
    val selectedBg: Color = Kaki,
    val selectedFg: Color = Color.White,
    val unselectedBg: Color = Color.White,
    val unselectedFg: Color = Kaki,
    val border: Color = Kaki,
)

// 사이즈 스펙
data class ChipSpec(
    val height: Dp,
    val minWidth: Dp,
    val corner: Dp,
    val borderWidth: Dp,
    val textStyle: @Composable () -> TextStyle
)

// 미리 정의한 프리셋
object ChipSpecs {
    val Large = ChipSpec(
        height = 44.dp, minWidth = 100.dp, corner = 22.dp, borderWidth = 1.5.dp,
        textStyle = { MaterialTheme.typography.titleMedium }
    )
    val Medium = ChipSpec(
        height = 36.dp, minWidth = 100.dp, corner = 18.dp, borderWidth = 1.dp,
        textStyle = { MaterialTheme.typography.labelLarge }
    )
    val Small = ChipSpec(
        height = 55.dp, minWidth = 100.dp, corner = 15.dp, borderWidth = 1.dp,
        textStyle = { MaterialTheme.typography.labelLarge }
    )
}

// 공통 베이스 Chip
@Composable
fun AppChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    palette: ChipPalette = ChipPalette(),
    spec: ChipSpec = ChipSpecs.Medium, // 기본사이즈
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val colors = FilterChipDefaults.filterChipColors(
        containerColor = if (selected) palette.selectedBg else palette.unselectedBg,
        labelColor = if (selected) palette.selectedFg else palette.unselectedFg,
        selectedContainerColor = palette.selectedBg,
        selectedLabelColor = palette.selectedFg
    )
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(spec.corner),
        color = if (selected) palette.selectedBg else palette.unselectedBg,
        contentColor = if (selected) palette.selectedFg else palette.unselectedFg,
        border = if (selected) null else BorderStroke(spec.borderWidth, palette.border),
        modifier = modifier
            .height(spec.height)
            .defaultMinSize(minWidth = spec.minWidth)

    ) {
        Box(
            modifier = modifier
                //.fillMaxWidth()
                //.height(20.dp)
                .padding(horizontal = 12.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = spec.textStyle(),
                textAlign = TextAlign.Center,
               // maxLines = 1, //한 줄 고정
                overflow = TextOverflow.Clip
            )
        }
    }
}