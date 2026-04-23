package com.doctool.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.doctool.data.model.FormatParams

@Composable
fun ParamEditor(
    params: FormatParams,
    onParamsChange: (FormatParams) -> Unit,
    modifier: Modifier = Modifier
) {
    var fontSize by remember { mutableFloatStateOf(params.fontSize.toFloat()) }
    var lineSpacing by remember { mutableFloatStateOf(params.lineSpacing) }
    var selectedAlignment by remember { mutableStateOf(params.alignment) }
    
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "格式编辑",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 字体大小
        Text(text = "字体大小: ${fontSize.toInt()}pt")
        Slider(
            value = fontSize,
            onValueChange = {
                fontSize = it
                onParamsChange(params.copy(fontSize = it.toInt()))
            },
            valueRange = 8f..30f
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 行间距
        Text(text = "行间距: %.1f".format(lineSpacing))
        Slider(
            value = lineSpacing,
            onValueChange = {
                lineSpacing = it
                onParamsChange(params.copy(lineSpacing = it))
            },
            valueRange = 1.0f..3.0f
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 对齐方式
        Text(text = "对齐方式")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedAlignment == "left",
                onClick = {
                    selectedAlignment = "left"
                    onParamsChange(params.copy(alignment = "left"))
                },
                label = { Text("左对齐") }
            )
            FilterChip(
                selected = selectedAlignment == "center",
                onClick = {
                    selectedAlignment = "center"
                    onParamsChange(params.copy(alignment = "center"))
                },
                label = { Text("居中") }
            )
            FilterChip(
                selected = selectedAlignment == "right",
                onClick = {
                    selectedAlignment = "right"
                    onParamsChange(params.copy(alignment = "right"))
                },
                label = { Text("右对齐") }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 预览区域
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "预览区域\n字体大小: ${fontSize.toInt()}pt\n行间距: %.1f\n对齐: ${getAlignmentText(selectedAlignment)}".format(lineSpacing),
                    fontSize = fontSize.dp
                )
            }
        }
    }
}

private fun getAlignmentText(alignment: String): String {
    return when (alignment) {
        "left" -> "左对齐"
        "center" -> "居中"
        "right" -> "右对齐"
        else -> "左对齐"
    }
}
