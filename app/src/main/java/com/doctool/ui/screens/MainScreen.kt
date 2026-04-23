package com.doctool.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doctool.ui.components.ParamEditor
import com.doctool.data.model.FormatParams

/**
 * 主屏幕
 */
@Composable
fun MainScreen(
    onNavigateToFileBrowser: () -> Unit,
    onNavigateToTemplateLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("文件操作", "格式编辑", "模板库")
    
    // 模拟文档参数
    val sampleParams = remember {
        FormatParams()
    }
    
    Column(modifier = modifier.fillMaxSize()) {
        // 顶部应用栏
        TopAppBar(
            title = { Text("DocTool") },
            actions = {
                IconButton(onClick = { /* 设置 */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "设置"
                    )
                }
            }
        )
        
        // 标签页
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        // 标签内容
        when (selectedTab) {
            0 -> FileOperationScreen(
                onBrowseFiles = onNavigateToFileBrowser,
                modifier = Modifier.fillMaxSize()
            )
            1 -> FormatEditScreen(
                documentParams = sampleParams,
                onParamsChanged = { /* 更新参数 */ },
                modifier = Modifier.fillMaxSize()
            )
            2 -> TemplateLibraryScreen(
                onBrowseTemplates = onNavigateToTemplateLibrary,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 文件操作屏幕
 */
@Composable
fun FileOperationScreen(
    onBrowseFiles: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 操作卡片
        OperationCard(
            title = "浏览文件",
            description = "选择要处理的文档",
            icon = Icons.Default.Folder,
            onClick = onBrowseFiles
        )
        
        OperationCard(
            title = "PDF转换",
            description = "PDF转图片、合并、拆分",
            icon = Icons.Default.PictureAsPdf,
            onClick = { /* TODO */ }
        )
        
        OperationCard(
            title = "格式转换",
            description = "图片格式转换",
            icon = Icons.Default.Image,
            onClick = { /* TODO */ }
        )
        
        OperationCard(
            title = "文档合并",
            description = "合并多个文档",
            icon = Icons.Default.Merge,
            onClick = { /* TODO */ }
        )
        
        OperationCard(
            title = "文档拆分",
            description = "拆分大文档",
            icon = Icons.Default.CallSplit,
            onClick = { /* TODO */ }
        )
    }
}

/**
 * 格式编辑屏幕
 */
@Composable
fun FormatEditScreen(
    documentParams: FormatParams,
    onParamsChanged: (FormatParams) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPreview by remember { mutableStateOf(true) }
    
    Column(modifier = modifier) {
        // 工具栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { /* 保存模板 */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("保存为模板")
            }
            
            Button(
                onClick = { /* 使用模板 */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("使用模板")
            }
            
            IconButton(onClick = { showPreview = !showPreview }) {
                Icon(
                    imageVector = if (showPreview) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPreview) "隐藏预览" else "显示预览"
                )
            }
        }
        
        if (showPreview) {
            // 分屏布局：预览 + 编辑
            Row(modifier = Modifier.fillMaxSize()) {
                // 文档预览（左侧60%）
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "文档预览区域\n\n字体: ${documentParams.fontName} ${documentParams.fontSize}pt\n行距: ${documentParams.lineSpacing.format(1)}倍\n对齐: ${documentParams.alignment.name}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                // 参数编辑（右侧40%）
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    ParamEditor(
                        params = documentParams,
                        onParamsChanged = onParamsChanged,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {
            // 全屏编辑模式
            ParamEditor(
                params = documentParams,
                onParamsChanged = onParamsChanged,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 模板库屏幕
 */
@Composable
fun TemplateLibraryScreen(
    onBrowseTemplates: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 搜索栏
        OutlinedTextField(
            value = "",
            onValueChange = { /* TODO */ },
            label = { Text("搜索模板...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
            modifier = Modifier.fillMaxWidth()
        )
        
        // 我的模板
        TemplateSection(
            title = "我的模板",
            count = 12,
            onClick = onBrowseTemplates
        )
        
        // 预设模板
        TemplateSection(
            title = "预设模板",
            count = 5,
            onClick = onBrowseTemplates
        )
        
        // 最近使用
        TemplateSection(
            title = "最近使用",
            count = 3,
            onClick = onBrowseTemplates
        )
        
        // 新建模板按钮
        Button(
            onClick = { /* 新建模板 */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text("新建模板")
        }
    }
}

/**
 * 操作卡片
 */
@Composable
fun OperationCard(
    title: String,
    description: String,
    icon: androidx.compose.material.icons.Icons.Filled,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp)
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 模板分区
 */
@Composable
fun TemplateSection(
    title: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$title ($count)",
                style = MaterialTheme.typography.titleMedium
            )
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "查看"
            )
        }
    }
}

/**
 * 导入图标（简化）
 */
object Icons {
    object Default {
        val Settings = androidx.compose.material.icons.Icons.Filled.Settings
        val Folder = androidx.compose.material.icons.Icons.Filled.Folder
        val PictureAsPdf = androidx.compose.material.icons.Icons.Filled.PictureAsPdf
        val Image = androidx.compose.material.icons.Icons.Filled.Image
        val Merge = androidx.compose.material.icons.Icons.Filled.CallMerge
        val CallSplit = androidx.compose.material.icons.Icons.Filled.CallSplit
        val Visibility = androidx.compose.material.icons.Icons.Filled.Visibility
        val VisibilityOff = androidx.compose.material.icons.Icons.Filled.VisibilityOff
        val Search = androidx.compose.material.icons.Icons.Filled.Search
        val ChevronRight = androidx.compose.material.icons.Icons.Filled.ChevronRight
    }
}