package com.doctool.template

import com.doctool.data.model.FormatParams
import com.doctool.data.model.FormatTemplate
import com.doctool.data.model.TemplateFilter
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * 模板管理器接口
 */
interface TemplateManager {
    
    /**
     * 获取所有模板
     */
    fun getAllTemplates(): Flow<List<FormatTemplate>>
    
    /**
     * 根据ID获取模板
     */
    suspend fun getTemplateById(id: String): FormatTemplate?
    
    /**
     * 保存模板
     */
    suspend fun saveTemplate(template: FormatTemplate): Result<Unit>
    
    /**
     * 更新模板
     */
    suspend fun updateTemplate(template: FormatTemplate): Result<Unit>
    
    /**
     * 删除模板
     */
    suspend fun deleteTemplate(id: String): Result<Unit>
    
    /**
     * 搜索模板
     */
    fun searchTemplates(filter: TemplateFilter): Flow<List<FormatTemplate>>
    
    /**
     * 应用模板到文档
     */
    suspend fun applyTemplate(document: File, template: FormatTemplate): Result<FormatParams>
    
    /**
     * 从当前参数创建模板
     */
    suspend fun createTemplateFromParams(
        name: String,
        params: FormatParams,
        tags: List<String> = emptyList(),
        previewImage: ByteArray? = null
    ): Result<FormatTemplate>
    
    /**
     * 导出模板为文件
     */
    suspend fun exportTemplate(template: FormatTemplate, outputFile: File): Result<Unit>
    
    /**
     * 从文件导入模板
     */
    suspend fun importTemplate(templateFile: File): Result<FormatTemplate>
    
    /**
     * 增加模板使用计数
     */
    suspend fun incrementUsageCount(templateId: String): Result<Unit>
    
    /**
     * 获取最常用模板
     */
    suspend fun getMostUsedTemplates(limit: Int = 5): List<FormatTemplate>
    
    /**
     * 根据文档推荐模板
     */
    suspend fun recommendTemplatesForDocument(document: File): List<FormatTemplate>
}

/**
 * 模板操作结果
 */
sealed class TemplateResult<T> {
    data class Success<T>(val data: T) : TemplateResult<T>()
    data class Error<T>(val message: String, val cause: Throwable? = null) : TemplateResult<T>()
}

/**
 * 模板验证器
 */
object TemplateValidator {
    
    fun validateTemplateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("模板名称不能为空")
            name.length > 50 -> ValidationResult.Error("模板名称不能超过50个字符")
            name.contains("/") || name.contains("\\") -> ValidationResult.Error("模板名称不能包含路径分隔符")
            else -> ValidationResult.Success
        }
    }
    
    fun validateTags(tags: List<String>): ValidationResult {
        if (tags.size > 10) {
            return ValidationResult.Error("标签数量不能超过10个")
        }
        
        tags.forEach { tag ->
            if (tag.isBlank()) {
                return ValidationResult.Error("标签不能为空")
            }
            if (tag.length > 20) {
                return ValidationResult.Error("单个标签不能超过20个字符")
            }
            if (tag.contains(",") || tag.contains(";")) {
                return ValidationResult.Error("标签不能包含逗号或分号")
            }
        }
        
        return ValidationResult.Success
    }
    
    fun validateParams(params: FormatParams): ValidationResult {
        // 验证字体大小
        if (params.fontSize < 6f || params.fontSize > 72f) {
            return ValidationResult.Error("字体大小必须在6pt到72pt之间")
        }
        
        // 验证行间距
        if (params.lineSpacing < 0.8f || params.lineSpacing > 3.0f) {
            return ValidationResult.Error("行间距必须在0.8到3.0之间")
        }
        
        // 验证页边距（不能为负数）
        if (params.margins.top < 0 || params.margins.bottom < 0 || 
            params.margins.left < 0 || params.margins.right < 0) {
            return ValidationResult.Error("页边距不能为负数")
        }
        
        // 验证页边距（不能太大）
        val maxMargin = 200f
        if (params.margins.top > maxMargin || params.margins.bottom > maxMargin ||
            params.margins.left > maxMargin || params.margins.right > maxMargin) {
            return ValidationResult.Error("页边距不能超过200pt")
        }
        
        return ValidationResult.Success
    }
}

/**
 * 验证结果
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}

/**
 * 模板匹配器
 */
object TemplateMatcher {
    
    /**
     * 计算模板与文档的匹配度
     */
    fun calculateMatchScore(template: FormatTemplate, documentParams: FormatParams): Float {
        var score = 0f
        val totalWeight = 6f // 总权重
        
        // 1. 页面尺寸匹配（权重1）
        if (template.params.pageSize == documentParams.pageSize) {
            score += 1f
        }
        
        // 2. 字体匹配（权重2）
        if (template.params.fontName == documentParams.fontName) {
            score += 0.5f
        }
        
        val fontSizeDiff = kotlin.math.abs(template.params.fontSize - documentParams.fontSize)
        if (fontSizeDiff <= 2f) {
            score += 0.5f
        }
        
        // 3. 行间距匹配（权重1）
        val lineSpacingDiff = kotlin.math.abs(template.params.lineSpacing - documentParams.lineSpacing)
        if (lineSpacingDiff <= 0.3f) {
            score += 1f
        }
        
        // 4. 对齐方式匹配（权重1）
        if (template.params.alignment == documentParams.alignment) {
            score += 1f
        }
        
        // 5. 页边距匹配（权重1）
        val marginDiff = calculateMarginDiff(template.params.margins, documentParams.margins)
        if (marginDiff <= 20f) {
            score += 1f
        }
        
        return score / totalWeight
    }
    
    private fun calculateMarginDiff(m1: com.doctool.data.model.Margins, m2: com.doctool.data.model.Margins): Float {
        return kotlin.math.abs(m1.top - m2.top) +
               kotlin.math.abs(m1.bottom - m2.bottom) +
               kotlin.math.abs(m1.left - m2.left) +
               kotlin.math.abs(m1.right - m2.right)
    }
    
    /**
     * 推荐最适合的模板
     */
    fun recommendBestTemplate(
        templates: List<FormatTemplate>,
        documentParams: FormatParams,
        minMatchScore: Float = 0.6f
    ): FormatTemplate? {
        return templates
            .map { template ->
                template to calculateMatchScore(template, documentParams)
            }
            .filter { (_, score) -> score >= minMatchScore }
            .maxByOrNull { (_, score) -> score }
            ?.first
    }
}