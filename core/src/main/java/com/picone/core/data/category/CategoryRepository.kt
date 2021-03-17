package com.picone.core.data.category

import com.picone.core.domain.entity.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDaoImpl: CategoryDaoImpl) {

    suspend fun getAllCategories() : List<Category>{
        return categoryDaoImpl.getAllCategories()
    }

    suspend fun addNewCategory(category: Category){
        categoryDaoImpl.addNewCategory(category)
    }
}