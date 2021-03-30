package com.picone.core.data

import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task

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

    private val TASKS: List<Task> = listOf(
        Task(1, 1, "task manager", "task application description", 1, null),
        Task(2, 1, "Account manager", "account application  description", 2, 3),
        Task(3, 2, "Flutter", "to follow that tool", 2, null)
    )

    fun generatedTasks(): MutableList<Task> {
        return TASKS.toMutableList()
    }
}