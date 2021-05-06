package com.picone.core.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.picone.core.util.Constants.TASK_TABLE_NAME
import com.picone.core.util.Constants.UNKNOWN
import java.util.*

@Entity(
    tableName = TASK_TABLE_NAME, foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId")
    )]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(index = true) val categoryId: Int,
    override val name: String = UNKNOWN,
    override val description: String = UNKNOWN,
    var importance: Int,
    val creation: Date,
    override var start: Date?,
    override var deadLine: Date?,
    override var close: Date?
):BaseTask(name, description, start, deadLine, close)