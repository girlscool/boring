package com.doctool.template.impl

import com.doctool.data.model.FormatParams
import com.doctool.data.model.FormatTemplate
import com.doctool.data.model.TemplateFilter
import com.doctool.template.TemplateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.*

/**
 * 内存模板管理器
 * 用于在没有数据库的情况下提供模板功能
 */
class MemoryTemplateManager : TemplateManager {
    
    private val templates = MutableStateFlow(mutableListOf<FormatTemplate>())
    private val presetTemplates = FormatTemplate.PRESET_TEMPLATES.toMutableList()
    
    init {
        // 初始化时添加预设模板
        templates.value.addAll(presetTemplates)
    }
    
    override fun getAllTemplates(): Flow<List<FormatTemplate>> {
        return templates.map { it.sortedByDescending { t -> t.updated } }
    }
    
    override suspend fun getTemplateById(id: String): FormatTemplate? {
        return templates.value.find { it.id == id }
    }
    
    override suspend fun saveTemplate(template: FormatTemplate): Result<Unit> {
        return try {
            templates.value.add(template)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateTemplate(template: FormatTemplate): Result<Unit> {
        return try {
            val index = templates.value.indexOfFirst { it.id == template.id }
            if (index >= 0) {
                templates.value[index] = template.copy(updated = Date().time)
                Result.success(Unit)
            } else {
                Result.failure(NoSuchElementException("Template not found: ${template.id}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteTemplate(id: String): Result<Unit> {
        return try {
            val template = templates.value.find { it.id == id }
            if (template != null && !template.isPreset) {
                templates.value.remove(template)
                Result.success(Unit)
            } else if (template?.isPreset == true) {
                Result.failure(IllegalStateException("Cannot delete preset template"))
            } else {
                Result.failure(NoSuchElementException("Template not found: $id"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchTemplates(filter: TemplateFilter): Flow<List<FormatTemplate>> {
        return templates.map { allTemplates ->
            allTemplates.filter { template ->
                // 按查询词过滤
                val matchesQuery = filter.query.isBlank() ||
                        template.name.contains(filter.query, ignoreCase = true) ||
                        template.tags.any { it.contains(filter.query, ignoreCase = true) }
                
                // 按标签过滤
                val matchesTags = filter.tags.isEmpty() ||
                        template.tags.any { tag -> filter.tags.any { it.equals(tag, ignoreCase = true) } }
                
                // 按类型过滤
                val matchesType = (filter.showPresets && template.isPreset) ||
                        (filter.showUserTemplates && !template.isPreset)
                
                matchesQuery && matchesTags && matchesType
            }.sortedWith(
                when (filter.sortBy) {
                    TemplateFilter.SortBy.RECENT -> compareByDescending { it.updated }
                    TemplateFilter.SortBy.NAME -> compareBy { it.name }
                    TemplateFilter.SortBy.USAGE -> compareByDescending { it.usageCount }
                    TemplateFilter.SortBy.CREATED -> compareByDescending { it.created }
                }
            )
        }
    }
    
    override suspend fun applyTemplate(document: File, template: FormatTemplate): Result<FormatParams> {
        return try {
            // 模拟应用模板
            kotlinx.coroutines.delay(300)
            
            // 增加使用计数
            val updatedTemplate = template.copy(
                usageCount = template.usageCount + 1,
                updated = Date().time
            )
            updateTemplate(updatedTemplate)
            
            Result.success(template.params)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createTemplateFromParams(
        name: String,
        params: FormatParams,
        tags: List<String>,
        previewImage: ByteArray?
    ): Result<FormatTemplate> {
        return try {
            val template = FormatTemplate(
                name = name,
                params = params,
                tags = tags,
                previewImagePath = previewImage?.let { 
                    // 模拟保存预览图
                    "/previews/${UUID.randomUUID()}.png"
                }
            )
            
            saveTemplate(template)
            Result.success(template)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun exportTemplate(template: FormatTemplate, outputFile: File): Result<Unit> {
        return try {
            // 模拟导出为JSON
            val json = """
                {
                    "template": {
                        "id": "${template.id}",
                        "name": "${template.name}",
                        "created": ${template.created},
                        "params": {
                            "fontName": "${template.params.fontName}",
                            "fontSize": ${template.params.fontSize},
                            "lineSpacing": ${template.params.lineSpacing}
                        },
                        "tags": ${template.tags.joinToString(", ", "[", "]") { "\"$it\"" }}
                    }
                }
            """.trimIndent()
            
            outputFile.writeText(json)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun importTemplate(templateFile: File): Result<FormatTemplate> {
        return try {
            // 模拟从JSON导入
            val content = templateFile.readText()
            
            // 简单解析（实际应使用JSON解析器）
            val nameMatch = Regex("\"name\"\\s*:\\s*\"([^\"]+)\"").find(content)
            val fontNameMatch = Regex("\"fontName\"\\s*:\\s*\"([^\"]+)\"").find(content)
            val fontSizeMatch = Regex("\"fontSize\"\\s*:\\s*(\\d+\\.?\\d*)").find(content)
            
            val template = FormatTemplate(
                name = nameMatch?.groupValues?.get(1) ?: "导入的模板",
                params = FormatParams(
                    fontName = fontNameMatch?.groupValues?.get(1) ?: "宋体",
                    fontSize = fontSizeMatch?.groupValues?.get(1)?.toFloatOrNull() ?: 12f
                ),
                tags = listOf("导入")
            )
            
            saveTemplate(template)
            Result.success(template)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun incrementUsageCount(templateId: String): Result<Unit> {
        return try {
            val template = getTemplateById(templateId)
            if (template != null) {
                val updated = template.copy(
                    usageCount = template.usageCount + 1,
                    updated = Date().time
                )
                updateTemplate(updated)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMostUsedTemplates(limit: Int): List<FormatTemplate> {
        return templates.value
            .sortedByDescending { it.usageCount }
            .take(limit)
    }
    
    override suspend fun recommendTemplatesForDocument(document: File): List<FormatTemplate> {
        // 基于文件扩展名的简单推荐
        val extension = document.extension.lowercase()
        
        return when (extension) {
            "pdf" -> templates.value.filter { it.tags.contains("报告") || it.tags.contains("正式") }
            "doc", "docx" -> templates.value.filter { it.tags.contains("文档") || it.tags.contains("办公") }
            "txt" -> templates.value.filter { it.tags.contains("简洁") || it.tags.contains("文本") }
            else -> templates.value.take(3)
        }
    }
}