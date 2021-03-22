package com.picone.core.domain.interactor.category

import com.picone.core.data.category.CategoryRepository
import com.picone.core.domain.entity.Category
import javax.inject.Inject

class AddNewCategoryInteractor @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend fun addNewCategory(category: Category){
        categoryRepository.addNewCategory(category)
    }
}