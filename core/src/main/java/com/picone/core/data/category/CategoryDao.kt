package com.picone.core.data.category

import androidx.room.Dao
import androidx.room.Query
import com.picone.core.domain.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT*FROM category_table")
    fun getAllCategories():List<Category>
}