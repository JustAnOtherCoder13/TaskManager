package com.picone.core.util

import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import java.util.*

object Constants {

    const val UNKNOWN = "UNKNOWN"

    const val CATEGORY_TABLE_NAME = "category_table"
    const val PROJECT_TABLE_NAME = "project_table"
    const val TASK_TABLE_NAME = "task_table"
    const val UNDER_STAIN_TABLE_NAME = "under_stain_table"

    const val IMPORTANCE_IMPORTANT = 0
    const val IMPORTANCE_NORMAL = 1
    const val IMPORTANCE_UNIMPORTANT = 2

    const val FIRST_ELEMENT = 0
    const val TASK_ID = "taskId"
    const val PROJECT_ID = "projectId"

    const val WHAT_IS_ADD = "what is add"
    const val ADD_PROJECT = 0
    const val ADD_TASK = 1
    const val ADD_UNDER_STAIN = 2

    private val calendar: Calendar = Calendar.getInstance()

    val MY_YEAR = calendar.get(Calendar.YEAR)
    val MY_MONTH = calendar.get(Calendar.MONTH)
    val MY_DAY = calendar.get(Calendar.DAY_OF_MONTH)

    //compose const
    const val CATEGORY = "Category"
    const val TASK = "Task"
    const val PROJECT = "Project"

    const val DELETE = "Delete"
    const val EDIT = "Edit"
    const val START ="Start"
    const val CLOSE = "Close"

    const val PASS_TO_TASK = "Pass to Task"

    const val ALL = "All"
    const val IMPORTANT = "Important"
    const val NORMAL = "Normal"
    const val UNIMPORTANT = "Unimportant"

    val IMPORTANCE_LIST : List<String> = listOf(UNIMPORTANT,NORMAL,IMPORTANT)

    const val KEY_TASK = "task"
    const val KEY_ITEM = "item"
    const val KEY_EDIT_TASK = "edit task"
    const val KEY_EDIT_PROJECT = "edit project"

    val UnknownTask = Task(-1, 0, "task not found", "", -1, Calendar.getInstance().time, null, null, null)

    val UnknownProject = Project(-1,0,"unknown Project", "")

    enum class COMPLETION_STATE{
        DEFAULT,
        ON_LOADING,
        ON_SUCCESS,
        ON_ERROR
    }

}