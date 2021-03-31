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


}