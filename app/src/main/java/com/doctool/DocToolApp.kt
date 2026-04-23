package com.doctool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.doctool.ui.screens.MainScreen
import com.doctool.ui.theme.DocToolTheme

/**
 * DocTool 主应用
 */
class DocToolApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            DocToolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onNavigateToFileBrowser = {
                            // 打开文件浏览器
                        },
                        onNavigateToTemplateLibrary = {
                            // 打开模板库
                        }
                    )
                }
            }
        }
    }
}

/**
 * 应用主题
 */
@Composable
fun DocToolAppPreview() {
    DocToolTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(
                onNavigateToFileBrowser = {},
                onNavigateToTemplateLibrary = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDocToolApp() {
    DocToolAppPreview()
}