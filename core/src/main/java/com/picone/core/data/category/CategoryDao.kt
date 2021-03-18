package com.picone.core.data.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.picone.core.domain.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT*FROM category_table")
    fun getAllCategories():Flow<List<Category>>

    @Insert
    suspend fun addNewCategory(category: Category)
}