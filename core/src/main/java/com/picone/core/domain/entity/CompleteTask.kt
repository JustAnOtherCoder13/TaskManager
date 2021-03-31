package com.picone.core.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteTask(
    @Embedded val task: Task
    ) {

    @Relation(parentColumn = "id",entityColumn = "taskId",entity = UnderStain::class)
    lateinit var underStainsForTask:List<UnderStain>

    @Relation(parentColumn = "categoryId",entityColumn = "id",entity = Category::class)
    lateinit var categoryForTask:Category
}