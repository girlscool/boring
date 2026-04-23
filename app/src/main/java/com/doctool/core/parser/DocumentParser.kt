package com.doctool.core.parser

import com.doctool.data.model.FormatParams
import java.io.File

/**
 * 文档解析器接口
 */
interface DocumentParser {
    
    /**
     * 支持的文档格式
     */
    val supportedFormats: List<String>
    
    /**
     * 解析文档格式参数
     * @param file 文档文件
     * @return 格式参数，解析失败返回null
     */
    suspend fun parseFormatParams(file: File): FormatParams?
    
    /**
     * 验证文档是否可解析
     */
    suspend fun validateDocument(file: File): Boolean
    
    /**
     * 获取文档基本信息
     */
    suspend fun getDocumentInfo(file: File): DocumentInfo
}

/**
 * 文档信息
 */
data class DocumentInfo(
    val fileName: String,
    val fileSize: Long,
    val pageCount: Int,
    val format: String,
    val createdTime: Long? = null,
    val modifiedTime: Long? = null,
    val isEncrypted: Boolean = false,
    val isCorrupted: Boolean = false
)

/**
 * 解析结果
 */
data class ParseResult(
    val success: Boolean,
    val params: FormatParams? = null,
    val info: DocumentInfo? = null,
    val error: String? = null,
    val warnings: List<String> = emptyList()
)

/**
 * 字体识别结果
 */
data class FontAnalysis(
    val fontName: String,
    val fontSize: Float, // 单位：pt
    val fontColor: Int,  // ARGB
    val isBold: Boolean,
    val isItalic: Boolean,
    val isUnderline: Boolean,
    val usagePercentage: Float // 使用百分比
)

/**
 * 段落分析结果
 */
data class ParagraphAnalysis(
    val lineSpacing: Float, // 行间距倍数
    val spacingBefore: Float, // 段前距（pt）
    val spacingAfter: Float,  // 段后距（pt）
    val alignment: String,    // 对齐方式
    val indent: Float,        // 缩进（pt）
    val paragraphCount: Int   // 段落数量
)

/**
 * 页面分析结果
 */
data class PageAnalysis(
    val pageSize: Pair<Float, Float>, // 宽高（pt）
    val margins: Quadruple<Float>,    // 上下左右边距
    val hasHeader: Boolean,
    val hasFooter: Boolean,
    val pageNumber: Int
)

/**
 * 四元组
 */
data class Quadruple<T>(
    val first: T,
    val second: T,
    val third: T,
    val fourth: T
)