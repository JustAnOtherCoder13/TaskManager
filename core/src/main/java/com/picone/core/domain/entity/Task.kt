package com.picone.core.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.picone.core.util.Constants.TASK_TABLE_NAME

@Entity(tableName = TASK_TABLE_NAME,foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("categoryId")
)])
data class Task(
    @PrimaryKey(autoGenerate = true)val id:Int,

    @ColumnInfo(index = true)val categoryId:Int,
    var name:String?,
    var description:String?,
    var importance:Int,
    val creation:Int,
    var close:Int?
    )
