package com.picone.core.data.category

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryDaoImpl @Inject constructor(
taskDatabase: TaskDatabase
){
    private val categoryDao:CategoryDao = taskDatabase.categoryDao()

     fun getAllCategories():Flow<List<Category>>{
        return categoryDao.getAllCategories()
    }

    suspend fun addNewCategory(category: Category){
        categoryDao.addNewCategory(category)
    }
}