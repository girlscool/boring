package com.doctool.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 格式模板
 */
@Entity(tableName = "templates")
data class FormatTemplate(
    @PrimaryKey
    val id: String = generateId(),
    val name: String,
    val created: Long = Date().time,
    val updated: Long = Date().time,
    val params: FormatParams,
    val previewImagePath: String? = null,
    val tags: List<String> = emptyList(),
    val isPreset: Boolean = false,
    val usageCount: Int = 0
) {
    companion object {
        private fun generateId(): String {
            return "template_${System.currentTimeMillis()}_${(1000..9999).random()}"
        }
        
        // 预设模板
        val PRESET_TEMPLATES = listOf(
            FormatTemplate(
                id = "preset_formal_report",
                name = "正式报告",
                params = FormatParams(
                    pageSize = PageSize.A4,
                    margins = Margins.default(),
                    fontName = "宋体",
                    fontSize = 12f,
                    lineSpacing = 1.5f,
                    paragraphSpacing = ParagraphSpacing(6f, 6f),
                    alignment = Alignment.START,
                    indent = Indent.default()
                ),
                tags = listOf("报告", "正式", "商务"),
                isPreset = true
            ),
            FormatTemplate(
                id = "preset_resume",
                name = "简洁简历",
                params = FormatParams(
                    pageSize = PageSize.A4,
                    margins = Margins.narrow(),
                    fontName = "微软雅黑",
                    fontSize = 11f,
                    lineSpacing = 1.2f,
                    paragraphSpacing = ParagraphSpacing(4f, 4f),
                    alignment = Alignment.START,
                    indent = Indent.none()
                ),
                tags = listOf("简历", "简洁", "求职"),
                isPreset = true
            ),
            FormatTemplate(
                id = "preset_meeting_minutes",
                name = "会议纪要",
                params = FormatParams(
                    pageSize = PageSize.A4,
                    margins = Margins.default(),
                    fontName = "楷体",
                    fontSize = 12f,
                    lineSpacing = 1.8f,
                    paragraphSpacing = ParagraphSpacing(8f, 8f),
                    alignment = Alignment.JUSTIFIED,
                    indent = Indent.default()
                ),
                tags = listOf("会议", "纪要", "工作"),
                isPreset = true
            ),
            FormatTemplate(
                id = "preset_academic_paper",
                name = "学术论文",
                params = FormatParams(
                    pageSize = PageSize.A4,
                    margins = Margins.wide(),
                    fontName = "Times New Roman",
                    fontSize = 12f,
                    lineSpacing = 2.0f,
                    paragraphSpacing = ParagraphSpacing(12f, 12f),
                    alignment = Alignment.JUSTIFIED,
                    indent = Indent.hanging()
                ),
                tags = listOf("学术", "论文", "研究"),
                isPreset = true
            ),
            FormatTemplate(
                id = "preset_newsletter",
                name = "新闻稿",
                params = FormatParams(
                    pageSize = PageSize.A4,
                    margins = Margins.narrow(),
                    fontName = "黑体",
                    fontSize = 14f,
                    fontColor = Color(0xFF333333),
                    lineSpacing = 1.6f,
                    paragraphSpacing = ParagraphSpacing(10f, 10f),
                    alignment = Alignment.CENTER,
                    indent = Indent.none()
                ),
                tags = listOf("新闻", "宣传", "媒体"),
                isPreset = true
            )
        )
    }
}

/**
 * 模板分类
 */
data class TemplateCategory(
    val name: String,
    val templates: List<FormatTemplate>,
    val iconRes: String? = null
)

/**
 * 模板搜索条件
 */
data class TemplateFilter(
    val query: String = "",
    val tags: List<String> = emptyList(),
    val sortBy: SortBy = SortBy.RECENT,
    val showPresets: Boolean = true,
    val showUserTemplates: Boolean = true
) {
    enum class SortBy {
        RECENT, NAME, USAGE, CREATED
    }
}