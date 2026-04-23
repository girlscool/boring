package com.doctool.template.impl

import com.doctool.data.model.FormatParams
import com.doctool.data.model.FormatTemplate
import com.doctool.template.TemplateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemoryTemplateManager : TemplateManager {
    private val templates = MutableStateFlow<List<FormatTemplate>>(
        FormatParams.DEFAULT_TEMPLATES
    )

    override fun getAllTemplates(): Flow<List<FormatTemplate>> = templates.asStateFlow()

    override suspend fun getTemplateById(id: String): FormatTemplate? {
        return templates.value.find { it.id == id }
    }

    override suspend fun saveTemplate(template: FormatTemplate) {
        templates.value = templates.value + template
    }

    override suspend fun updateTemplate(template: FormatTemplate) {
        templates.value = templates.value.map {
            if (it.id == template.id) template else it
        }
    }

    override suspend fun deleteTemplate(id: String) {
        templates.value = templates.value.filter { it.id != id }
    }

    override suspend fun createTemplateFromParams(name: String, params: FormatParams): FormatTemplate {
        val template = params.toTemplate(name)
        templates.value = templates.value + template
        return template
    }
}
