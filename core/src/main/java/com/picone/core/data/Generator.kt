package com.picone.core.data

import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import java.util.*

object Generator {

    private val CATEGORIES: List<Category> = listOf(
        Category(1, 0xffffeb46, "for work"),
        Category(2, 0xff91a4fc, "learnings")
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
        Task(1, 1, "task manager", "task application description", 0,Calendar.getInstance().time, Calendar.getInstance().time,null,null),
        Task(2, 1, "Account manager", "account application  description", 2,Calendar.getInstance().time, Calendar.getInstance().time,null,null),
        Task(3, 2, "React native", "to make me bankable", 1,Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time,null)
    )

    fun generatedTasks(): MutableList<Task> {
        return TASKS.toMutableList()
    }

    private val UNDER_STAINS: List<UnderStain> = listOf(
        UnderStain(1, 1, "technical documentation", "functional description, technical description", Calendar.getInstance().time, null,null),
        UnderStain(2, 1, "implement architecture", "clean architecture, two module, one for other for data", Calendar.getInstance().time, Calendar.getInstance().time,null),
        UnderStain(3, 2, "react native basics", "follow the online course on OpenClassrooms", Calendar.getInstance().time, null,null)
    )

    fun generatedUnderStains(): MutableList<UnderStain> {
        return UNDER_STAINS.toMutableList()
    }
}