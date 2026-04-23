package com.doctool.core.parser.impl

import com.doctool.data.model.FormatParams
import java.io.File

class PdfParser {
    
    fun parse(file: File): ParsedDocument {
        val params = analyzeDocument(file)
        return ParsedDocument(
            fileName = file.name,
            fileSize = file.length(),
            formatParams = params,
            pageCount = estimatePageCount(file)
        )
    }
    
    private fun analyzeDocument(file: File): FormatParams {
        val fileName = file.name.lowercase()
        
        return when {
            fileName.contains("resume") || fileName.contains("cv") -> FormatParams(
                fontName = "微软雅黑",
                fontSize = 11,
                lineSpacing = 1.2f,
                alignment = "left"
            )
            fileName.contains("report") -> FormatParams(
                fontName = "黑体",
                fontSize = 14,
                lineSpacing = 1.8f,
                alignment = "center"
            )
            else -> FormatParams(
                fontName = "宋体",
                fontSize = 12,
                lineSpacing = 1.5f,
                alignment = "left"
            )
        }
    }
    
    private fun estimatePageCount(file: File): Int {
        val sizeInKB = file.length() / 1024
        return when {
            sizeInKB < 100 -> 1
            sizeInKB < 500 -> (sizeInKB / 100).toInt().coerceAtLeast(1)
            sizeInKB < 2000 -> (sizeInKB / 200).toInt().coerceAtLeast(2)
            else -> (sizeInKB / 300).toInt().coerceAtLeast(5).coerceAtMost(100)
        }
    }
}

data class ParsedDocument(
    val fileName: String,
    val fileSize: Long,
    val formatParams: FormatParams,
    val pageCount: Int
)

data class TextMetrics(
    val averageCharHeight: Float,
    val charCount: Int
)
