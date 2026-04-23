package com.doctool.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.doctool.data.model.FormatParams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamEditor(
    params: FormatParams,
    onParamsChange: (FormatParams) -> Unit,
    modifier: Modifier = Modifier
) {
    var fontSize by remember { mutableStateOf(params.fontSize.toFloat()) }
    var lineSpacing by remember { mutableStateOf(params.lineSpacing) }
    var selectedAlignment by remember { mutableStateOf(params.alignment) }

    Column(modifier = modifier.padding(16.dp)) {
        Text("格式编辑", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Text("字体大小: ${fontSize.toInt()}pt")
        Slider(value = fontSize, onValueChange = { fontSize = it; onParamsChange(params.copy(fontSize = it.toInt())) }, valueRange = 8f..30f)

        Spacer(Modifier.height(12.dp))
        Text("行间距: %.1f".format(lineSpacing))
        Slider(value = lineSpacing, onValueChange = { lineSpacing = it; onParamsChange(params.copy(lineSpacing = it)) }, valueRange = 1.0f..3.0f)

        Spacer(Modifier.height(12.dp))
        Text("对齐方式")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = selectedAlignment == "left", onClick = { selectedAlignment = "left"; onParamsChange(params.copy(alignment = "left")) }, label = { Text("左对齐") })
            FilterChip(selected = selectedAlignment == "center", onClick = { selectedAlignment = "center"; onParamsChange(params.copy(alignment = "center")) }, label = { Text("居中") })
            FilterChip(selected = selectedAlignment == "right", onClick = { selectedAlignment = "right"; onParamsChange(params.copy(alignment = "right")) }, label = { Text("右对齐") })
        }

        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth().height(120.dp)) {
            Box(Modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.Center) {
                Text("预览区域\n字体: ${fontSize.toInt()}pt\n行距: %.1f\n对齐: $selectedAlignment".format(lineSpacing))
            }
        }
    }
}
