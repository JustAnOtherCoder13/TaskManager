package com.picone.core.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.picone.core.util.Constants.TASK_TABLE_NAME
import java.util.*

@Entity(
    tableName = TASK_TABLE_NAME, foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId")
    )]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(index = true) val categoryId: Int,
    val name: String,
    val description: String,
    var importance: Int,
    val creation: Date,
    var start: Date?,
    var deadLine: Date?,
    var close: Date?
)

