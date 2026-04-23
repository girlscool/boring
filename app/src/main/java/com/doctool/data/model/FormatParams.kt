package com.doctool.data.model

import androidx.compose.ui.graphics.Color

/**
 * 文档格式参数
 */
data class FormatParams(
    // 文档级参数
    val pageSize: PageSize = PageSize.A4,
    val margins: Margins = Margins.default(),
    
    // 字体参数
    val fontName: String = "宋体",
    val fontSize: Float = 12f,
    val fontColor: Color = Color.Black,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    
    // 段落参数
    val lineSpacing: Float = 1.5f,
    val paragraphSpacing: ParagraphSpacing = ParagraphSpacing.default(),
    val alignment: Alignment = Alignment.START,
    val indent: Indent = Indent.default(),
    
    // 表格样式（可选）
    val tableStyle: TableStyle? = null
)

/**
 * 页面尺寸
 */
enum class PageSize(val width: Float, val height: Float, val displayName: String) {
    A4(595f, 842f, "A4"),
    A3(842f, 1191f, "A3"),
    LETTER(612f, 792f, "Letter"),
    LEGAL(612f, 1008f, "Legal")
}

/**
 * 页边距
 */
data class Margins(
    val top: Float,
    val bottom: Float,
    val left: Float,
    val right: Float
) {
    companion object {
        fun default() = Margins(72f, 72f, 90f, 90f) // 1英寸=72pt
        fun narrow() = Margins(36f, 36f, 54f, 54f)
        fun wide() = Margins(90f, 90f, 108f, 108f)
    }
}

/**
 * 段落间距
 */
data class ParagraphSpacing(
    val before: Float, // 段前距
    val after: Float   // 段后距
) {
    companion object {
        fun default() = ParagraphSpacing(6f, 6f)
        fun compact() = ParagraphSpacing(3f, 3f)
        fun loose() = ParagraphSpacing(12f, 12f)
    }
}

/**
 * 对齐方式
 */
enum class Alignment {
    START, CENTER, END, JUSTIFIED
}

/**
 * 缩进设置
 */
data class Indent(
    val firstLine: Float, // 首行缩进
    val left: Float,      // 左缩进
    val right: Float      // 右缩进
) {
    companion object {
        fun default() = Indent(21f, 0f, 0f) // 首行缩进2字符
        fun none() = Indent(0f, 0f, 0f)
        fun hanging() = Indent(-21f, 21f, 0f) // 悬挂缩进
    }
}

/**
 * 表格样式
 */
data class TableStyle(
    val borderWidth: Float = 0.5f,
    val borderColor: Color = Color.Black,
    val cellPadding: Float = 4f,
    val headerBackground: Color = Color.LightGray,
    val textAlignment: Alignment = Alignment.START
)