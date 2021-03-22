package com.picone.core.data.category

import com.picone.core.domain.entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDaoImpl: CategoryDaoImpl) {

    fun getAllCategories() : Flow<List<Category>>{
        return categoryDaoImpl.getAllCategories()
    }

    suspend fun addNewCategory(category: Category){
        categoryDaoImpl.addNewCategory(category)
    }
}