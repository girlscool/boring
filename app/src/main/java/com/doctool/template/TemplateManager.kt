package com.doctool.template

import com.doctool.data.model.FormatParams
import com.doctool.data.model.FormatTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface TemplateManager {
    fun getAllTemplates(): Flow<List<FormatTemplate>>
    suspend fun getTemplateById(id: String): FormatTemplate?
    suspend fun saveTemplate(template: FormatTemplate)
    suspend fun updateTemplate(template: FormatTemplate)
    suspend fun deleteTemplate(id: String)
    suspend fun createTemplateFromParams(name: String, params: FormatParams): FormatTemplate
}
