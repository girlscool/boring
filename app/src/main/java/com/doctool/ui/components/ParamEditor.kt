package com.doctool.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doctool.data.model.*

/**
 * 参数编辑器组件
 */
@Composable
fun ParamEditor(
    params: FormatParams,
    onParamsChanged: (FormatParams) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentParams by remember { mutableStateOf(params) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 页面设置
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "页面设置",
                    style = MaterialTheme.typography.titleMedium
                )
                
                // 页面尺寸
                DropdownSetting(
                    label = "页面尺寸",
                    value = currentParams.pageSize.displayName,
                    options = PageSize.values().map { it.displayName },
                    onOptionSelected = { index ->
                        currentParams = currentParams.copy(
                            pageSize = PageSize.values()[index]
                        )
                        onParamsChanged(currentParams)
                    }
                )
                
                // 页边距
                MarginEditor(
                    margins = currentParams.margins,
                    onMarginsChanged = { newMargins ->
                        currentParams = currentParams.copy(margins = newMargins)
                        onParamsChanged(currentParams)
                    }
                )
            }
        }
        
        // 字体设置
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "字体设置",
                    style = MaterialTheme.typography.titleMedium
                )
                
                // 字体名称
                TextField(
                    value = currentParams.fontName,
                    onValueChange = { newName ->
                        currentParams = currentParams.copy(fontName = newName)
                        onParamsChanged(currentParams)
                    },
                    label = { Text("字体名称") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 字体大小
                SliderSetting(
                    label = "字体大小",
                    value = currentParams.fontSize,
                    valueRange = 6f..72f,
                    steps = 66,
                    valueSuffix = "pt",
                    onValueChange = { newSize ->
                        currentParams = currentParams.copy(fontSize = newSize)
                        onParamsChanged(currentParams)
                    }
                )
                
                // 字体样式
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CheckboxSetting(
                        label = "粗体",
                        checked = currentParams.bold,
                        onCheckedChange = { checked ->
                            currentParams = currentParams.copy(bold = checked)
                            onParamsChanged(currentParams)
                        }
                    )
                    
                    CheckboxSetting(
                        label = "斜体",
                        checked = currentParams.italic,
                        onCheckedChange = { checked ->
                            currentParams = currentParams.copy(italic = checked)
                            onParamsChanged(currentParams)
                        }
                    )
                    
                    CheckboxSetting(
                        label = "下划线",
                        checked = currentParams.underline,
                        onCheckedChange = { checked ->
                            currentParams = currentParams.copy(underline = checked)
                            onParamsChanged(currentParams)
                        }
                    )
                }
                
                // 字体颜色（简化版）
                Text(
                    text = "字体颜色: #${currentParams.fontColor.toArgb().toUInt().toString(16).takeLast(6)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        
        // 段落设置
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "段落设置",
                    style = MaterialTheme.typography.titleMedium
                )
                
                // 行间距
                SliderSetting(
                    label = "行间距",
                    value = currentParams.lineSpacing,
                    valueRange = 0.8f..3.0f,
                    steps = 22,
                    valueSuffix = "倍",
                    onValueChange = { newSpacing ->
                        currentParams = currentParams.copy(lineSpacing = newSpacing)
                        onParamsChanged(currentParams)
                    }
                )
                
                // 段落间距
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SliderSetting(
                        label = "段前距",
                        value = currentParams.paragraphSpacing.before,
                        valueRange = 0f..24f,
                        steps = 24,
                        valueSuffix = "pt",
                        modifier = Modifier.weight(1f),
                        onValueChange = { newBefore ->
                            currentParams = currentParams.copy(
                                paragraphSpacing = currentParams.paragraphSpacing.copy(before = newBefore)
                            )
                            onParamsChanged(currentParams)
                        }
                    )
                    
                    SliderSetting(
                        label = "段后距",
                        value = currentParams.paragraphSpacing.after,
                        valueRange = 0f..24f,
                        steps = 24,
                        valueSuffix = "pt",
                        modifier = Modifier.weight(1f),
                        onValueChange = { newAfter ->
                            currentParams = currentParams.copy(
                                paragraphSpacing = currentParams.paragraphSpacing.copy(after = newAfter)
                            )
                            onParamsChanged(currentParams)
                        }
                    )
                }
                
                // 对齐方式
                DropdownSetting(
                    label = "对齐方式",
                    value = currentParams.alignment.name,
                    options = Alignment.values().map { it.name },
                    onOptionSelected = { index ->
                        currentParams = currentParams.copy(
                            alignment = Alignment.values()[index]
                        )
                        onParamsChanged(currentParams)
                    }
                )
                
                // 缩进设置
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SliderSetting(
                        label = "首行缩进",
                        value = currentParams.indent.firstLine,
                        valueRange = 0f..42f,
                        steps = 42,
                        valueSuffix = "pt",
                        modifier = Modifier.weight(1f),
                        onValueChange = { newIndent ->
                            currentParams = currentParams.copy(
                                indent = currentParams.indent.copy(firstLine = newIndent)
                            )
                            onParamsChanged(currentParams)
                        }
                    )
                    
                    SliderSetting(
                        label = "左缩进",
                        value = currentParams.indent.left,
                        valueRange = 0f..72f,
                        steps = 72,
                        valueSuffix = "pt",
                        modifier = Modifier.weight(1f),
                        onValueChange = { newLeft ->
                            currentParams = currentParams.copy(
                                indent = currentParams.indent.copy(left = newLeft)
                            )
                            onParamsChanged(currentParams)
                        }
                    )
                }
            }
        }
        
        // 操作按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    // 应用更改
                    onParamsChanged(currentParams)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("应用更改")
            }
            
            Button(
                onClick = {
                    // 重置为原始值
                    currentParams = params
                    onParamsChanged(currentParams)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text("重置")
            }
        }
    }
}

/**
 * 下拉设置组件
 */
@Composable
fun DropdownSetting(
    label: String,
    value: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * 滑块设置组件
 */
@Composable
fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    valueSuffix: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "${value.format(1)}$valueSuffix",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 页边距编辑器
 */
@Composable
fun MarginEditor(
    margins: Margins,
    onMarginsChanged: (Margins) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "页边距 (pt)",
            style = MaterialTheme.typography.bodyMedium
        )
        
        // 上边距
        SliderSetting(
            label = "上边距",
            value = margins.top,
            valueRange = 0f..200f,
            steps = 200,
            valueSuffix = "pt",
            onValueChange = { newTop ->
                onMarginsChanged(margins.copy(top = newTop))
            }
        )
        
        // 下边距
        SliderSetting(
            label = "下边距",
            value = margins.bottom,
            valueRange = 0f..200f,
            steps = 200,
            valueSuffix = "pt",
            onValueChange = { newBottom ->
                onMarginsChanged(margins.copy(bottom = newBottom))
            }
        )
        
        // 左边距
        SliderSetting(
            label = "左边距",
            value = margins.left,
            valueRange = 0f..200f,
            steps = 200,
            valueSuffix = "pt",
            onValueChange = { newLeft ->
                onMarginsChanged(margins.copy(left = newLeft))
            }
        )
        
        // 右边距
        SliderSetting(
            label = "右边距",
            value = margins.right,
            valueRange = 0f..200f,
            steps = 200,
            valueSuffix = "pt",
            onValueChange = { newRight ->
                onMarginsChanged(margins.copy(right = newRight))
            }
        )
    }
}

/**
 * 复选框设置
 */
@Composable
fun CheckboxSetting(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * 格式化浮点数
 */
private fun Float.format(digits: Int): String {
    return "%.${digits}f".format(this)
}