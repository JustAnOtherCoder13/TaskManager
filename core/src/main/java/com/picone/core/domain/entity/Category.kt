package com.picone.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(@PrimaryKey(autoGenerate = true) val id:Int)