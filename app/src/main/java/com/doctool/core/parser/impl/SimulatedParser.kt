package com.doctool.core.parser.impl

import com.doctool.data.model.FormatParams
import java.io.File

class SimulatedParser {
    
    fun parse(file: File): ParsedDocument {
        val formatParams = analyzeFile(file)
        return ParsedDocument(
            fileName = file.name,
            fileSize = file.length(),
            formatParams = formatParams,
            pageCount = estimatePages(file)
        )
    }
    
    private fun analyzeFile(file: File): FormatParams {
        val name = file.name.lowercase()
        return when {
            name.contains("pdf") -> FormatParams(
                fontName = "宋体",
                fontSize = 12,
                lineSpacing = 1.5f,
                alignment = "left"
            )
            name.contains("doc") -> FormatParams(
                fontName = "微软雅黑",
                fontSize = 11,
                lineSpacing = 1.3f,
                alignment = "left"
            )
            else -> FormatParams(
                fontName = "黑体",
                fontSize = 14,
                lineSpacing = 1.8f,
                alignment = "center"
            )
        }
    }
    
    private fun estimatePages(file: File): Int {
        return (file.length() / 51200).toInt().coerceIn(1, 100)
    }
}
