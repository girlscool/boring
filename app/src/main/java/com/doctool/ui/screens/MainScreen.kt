package com.doctool.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.doctool.data.model.*
import com.doctool.ui.components.ParamEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onNavigateToFileBrowser: () -> Unit, onNavigateToTemplateLibrary: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var currentParams by remember { mutableStateOf(FormatParams()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("DocTool", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary))
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 }, icon = { Text("📝") }, label = { Text("格式编辑") })
                NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 }, icon = { Text("📋") }, label = { Text("模板库") })
                NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 }, icon = { Text("📂") }, label = { Text("文档") })
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> ParamEditor(params = currentParams, onParamsChange = { currentParams = it }, modifier = Modifier.padding(padding))
            1 -> TemplateLibrary(onApplyTemplate = { t -> currentParams = FormatParams(fontSize = t.fontSize, lineSpacing = t.lineSpacing, alignment = t.alignment) }, modifier = Modifier.padding(padding))
            2 -> DocumentOperations(onNavigateToFileBrowser = onNavigateToFileBrowser, modifier = Modifier.padding(padding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateLibrary(onApplyTemplate: (FormatTemplate) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text("模板库", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        for (t in FormatParams.DEFAULT_TEMPLATES) {
            Card(onClick = { onApplyTemplate(t) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text(t.name, fontWeight = FontWeight.Bold)
                    Text("字体: ${t.fontName}, 大小: ${t.fontSize}pt", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentOperations(onNavigateToFileBrowser: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text("文档操作", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("打开文档") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("保存文档") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("导出PDF") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("分享文档") }
    }
}

