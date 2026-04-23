package com.doctool.data.model

import java.util.Date

data class FormatTemplate(
    val id: String = generateId(),
    val name: String,
    val created: Long = Date().time,
    val updated: Long = Date().time,
    val fontName: String = "Default",
    val fontSize: Int = 12,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val lineSpacing: Float = 1.5f,
    val paragraphSpacing: Float = 6.0f,
    val alignment: String = "left",
    val firstLineIndent: Float = 0.0f,
    val pageMarginTop: Float = 25.4f,
    val pageMarginBottom: Float = 25.4f,
    val pageMarginLeft: Float = 31.7f,
    val pageMarginRight: Float = 31.7f
) {
    companion object {
        private var counter = 0L
        
        fun generateId(): String {
            counter++
            return "T${System.currentTimeMillis()}_$counter"
        }
    }
}

data class FormatParams(
    val fontName: String = "Default",
    val fontSize: Int = 12,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val lineSpacing: Float = 1.5f,
    val paragraphSpacing: Float = 6.0f,
    val alignment: String = "left",
    val firstLineIndent: Float = 25.0f,
    val pageMarginTop: Float = 25.4f,
    val pageMarginBottom: Float = 25.4f,
    val pageMarginLeft: Float = 31.7f,
    val pageMarginRight: Float = 31.7f
) {
    fun toTemplate(name: String): FormatTemplate {
        return FormatTemplate(
            name = name,
            fontName = fontName,
            fontSize = fontSize,
            isBold = isBold,
            isItalic = isItalic,
            lineSpacing = lineSpacing,
            paragraphSpacing = paragraphSpacing,
            alignment = alignment,
            firstLineIndent = firstLineIndent,
            pageMarginTop = pageMarginTop,
            pageMarginBottom = pageMarginBottom,
            pageMarginLeft = pageMarginLeft,
            pageMarginRight = pageMarginRight
        )
    }
    
    companion object {
        val EMPTY = FormatParams()
        
        val DEFAULT_TEMPLATES = listOf(
            FormatTemplate(
                id = "formal_report",
                name = "正式报告",
                fontName = "宋体",
                fontSize = 14,
                isBold = false,
                lineSpacing = 1.5f,
                alignment = "left",
                firstLineIndent = 25.0f
            ),
            FormatTemplate(
                id = "simple_resume",
                name = "简洁简历",
                fontName = "黑体",
                fontSize = 12,
                lineSpacing = 1.2f,
                alignment = "left"
            ),
            FormatTemplate(
                id = "meeting_minutes",
                name = "会议纪要",
                fontName = "微软雅黑",
                fontSize = 11,
                lineSpacing = 1.3f,
                alignment = "left"
            ),
            FormatTemplate(
                id = "academic_paper",
                name = "学术论文",
                fontName = "宋体",
                fontSize = 12,
                lineSpacing = 2.0f,
                alignment = "left",
                firstLineIndent = 25.0f
            ),
            FormatTemplate(
                id = "news_release",
                name = "新闻稿",
                fontName = "Arial",
                fontSize = 12,
                lineSpacing = 1.5f,
                alignment = "left"
            )
        )
    }
}
