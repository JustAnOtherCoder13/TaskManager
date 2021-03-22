package com.picone.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.picone.core.util.Constants.CATEGORY_TABLE_NAME

@Entity(tableName = CATEGORY_TABLE_NAME)
data class Category(
    @PrimaryKey(autoGenerate = true) val id:Int,

    var color:String?,
    val name:String?

    )
