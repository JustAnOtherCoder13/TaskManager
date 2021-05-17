package com.picone.core.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.picone.core.util.Constants.PROJECT_TABLE_NAME

@Entity(tableName = PROJECT_TABLE_NAME,foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("categoryId")
)])
data class Project(
    @PrimaryKey(autoGenerate = true)val id:Int,

    @ColumnInfo(index = true)val categoryId:Int,
    var name: String,
    var description: String
    )
