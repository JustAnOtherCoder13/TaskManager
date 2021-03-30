package com.picone.core.data

import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project

object Generator {

    private val CATEGORIES: List<Category> = listOf(
        Category(1, "red", "for work"),
        Category(2, "orange", "learnings")
    )

    fun generatedCategories(): MutableList<Category> {
        return CATEGORIES.toMutableList()
    }

    private val PROJECTS: List<Project> = listOf(
        Project(1, 1, "task manager project", "project task description"),
        Project(2, 1, "Personal account manager", "project account description"),
        Project(3, 2, "React native or flutter?", "what to learn first")
    )

    fun generatedProjects(): MutableList<Project> {
        return PROJECTS.toMutableList()
    }
}