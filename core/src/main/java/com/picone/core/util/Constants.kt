package com.picone.core.util

import java.text.DateFormat
import java.util.*

object Constants {

    const val CATEGORY_TABLE_NAME = "category_table"
    const val PROJECT_TABLE_NAME = "project_table"
    const val TASK_TABLE_NAME = "task_table"
    const val UNDER_STAIN_TABLE_NAME = "under_stain_table"

    val Date.medium:String
        get() = DateFormat.getDateInstance(DateFormat.MEDIUM).format(this)

    const val IMPORTANCE_IMPORTANT = 0
    const val IMPORTANCE_NORMAL = 1
    const val IMPORTANCE_UNIMPORTANT = 2

    const val FIRST_ELEMENT = 0
    const val TASK_ID = "taskId"

    const val WHAT_IS_ADD = "what is add"
    const val ADD_PROJECT = 0
    const val ADD_TASK = 1
    const val ADD_UNDER_STAIN = 2


}