package com.doctool.core.parser.impl

import android.content.Context
import com.doctool.core.parser.*
import com.doctool.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random

/**
 * 模拟文档解析器
 * 用于在没有实际PDF库的情况下提供功能
 */
class SimulatedParser(private val context: Context) : DocumentParser {
    
    override val supportedFormats = listOf("pdf", "doc", "docx", "txt", "png", "jpg")
    
    override suspend fun parseFormatParams(file: File): FormatParams {
        return withContext(Dispatchers.IO) {
            // 基于文件名的模拟分析
            val fileName = file.name.lowercase()
            val fileSize = file.length()
            
            // 根据文件特征返回不同的格式参数
            when {
                // PDF文档
                fileName.endsWith(".pdf") -> {
                    if (fileSize > 2 * 1024 * 1024) {
                        // 大PDF - 正式文档
                        FormatParams(
                            pageSize = PageSize.A4,
                            margins = Margins.default(),
                            fontName = "宋体",
                            fontSize = 12f,
                            lineSpacing = 1.5f,
                            paragraphSpacing = ParagraphSpacing(6f, 6f),
                            alignment = Alignment.START,
                            indent = Indent.default()
                        )
                    } else {
                        // 小PDF - 简洁文档
                        FormatParams(
                            pageSize = PageSize.A4,
                            margins = Margins.narrow(),
                            fontName = "微软雅黑",
                            fontSize = 11f,
                            lineSpacing = 1.2f,
                            paragraphSpacing = ParagraphSpacing(4f, 4f),
                            alignment = Alignment.START,
                            indent = Indent.none()
                        )
                    }
                }
                
                // Word文档
                fileName.endsWith(".doc") || fileName.endsWith(".docx") -> {
                    FormatParams(
                        pageSize = PageSize.A4,
                        margins = Margins.default(),
                        fontName = "Calibri",
                        fontSize = 11f,
                        lineSpacing = 1.15f,
                        paragraphSpacing = ParagraphSpacing(8f, 8f),
                        alignment = Alignment.JUSTIFIED,
                        indent = Indent.hanging()
                    )
                }
                
                // 图片文件
                fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> {
                    FormatParams(
                        pageSize = PageSize.A4,
                        margins = Margins.narrow(),
                        fontName = "Arial",
                        fontSize = 10f,
                        lineSpacing = 1.0f,
                        paragraphSpacing = ParagraphSpacing(0f, 0f),
                        alignment = Alignment.CENTER,
                        indent = Indent.none()
                    )
                }
                
                // 文本文件
                fileName.endsWith(".txt") -> {
                    FormatParams(
                        pageSize = PageSize.A4,
                        margins = Margins.wide(),
                        fontName = "Courier New",
                        fontSize = 12f,
                        lineSpacing = 1.0f,
                        paragraphSpacing = ParagraphSpacing(0f, 0f),
                        alignment = Alignment.START,
                        indent = Indent.none()
                    )
                }
                
                // 默认
                else -> FormatParams()
            }
        }
    }
    
    override suspend fun validateDocument(file: File): Boolean {
        return withContext(Dispatchers.IO) {
            file.exists() && file.isFile && file.length() > 0
        }
    }
    
    override suspend fun getDocumentInfo(file: File): DocumentInfo {
        return withContext(Dispatchers.IO) {
            val format = when {
                file.name.endsWith(".pdf") -> "PDF"
                file.name.endsWith(".doc") -> "DOC"
                file.name.endsWith(".docx") -> "DOCX"
                file.name.endsWith(".txt") -> "TXT"
                file.name.endsWith(".png") -> "PNG"
                file.name.endsWith(".jpg") || file.name.endsWith(".jpeg") -> "JPEG"
                else -> "UNKNOWN"
            }
            
            // 模拟页数（基于文件大小）
            val pageCount = when {
                file.length() < 100 * 1024 -> 1
                file.length() < 500 * 1024 -> Random.nextInt(1, 5)
                file.length() < 2 * 1024 * 1024 -> Random.nextInt(5, 20)
                else -> Random.nextInt(20, 100)
            }
            
            DocumentInfo(
                fileName = file.name,
                fileSize = file.length(),
                pageCount = pageCount,
                format = format,
                createdTime = null,
                modifiedTime = file.lastModified(),
                isEncrypted = false,
                isCorrupted = false
            )
        }
    }
    
    /**
     * 模拟应用格式更改
     */
    suspend fun applyFormatChanges(file: File, newParams: FormatParams): Boolean {
        return withContext(Dispatchers.IO) {
            // 模拟处理时间
            kotlinx.coroutines.delay(500)
            true
        }
    }
    
    /**
     * 模拟文档转换
     */
    suspend fun convertDocument(
        inputFile: File,
        outputFormat: String,
        outputPath: String
    ): Result<File> {
        return withContext(Dispatchers.IO) {
            kotlinx.coroutines.delay(1000) // 模拟转换时间
            
            val outputFile = File(outputPath)
            // 模拟创建输出文件
            outputFile.writeText("模拟转换结果 - 原文件: ${inputFile.name}, 目标格式: $outputFormat")
            
            Result.success(outputFile)
        }
    }
    
    /**
     * 模拟文档合并
     */
    suspend fun mergeDocuments(files: List<File>, outputPath: String): Result<File> {
        return withContext(Dispatchers.IO) {
            kotlinx.coroutines.delay(files.size * 500L) // 模拟合并时间
            
            val outputFile = File(outputPath)
            val mergedContent = files.joinToString("\n\n--- 文档分割线 ---\n\n") { file ->
                "文档: ${file.name}\n大小: ${file.length()} 字节\n格式: ${file.extension.uppercase()}"
            }
            
            outputFile.writeText("合并文档结果:\n\n$mergedContent")
            Result.success(outputFile)
        }
    }
}