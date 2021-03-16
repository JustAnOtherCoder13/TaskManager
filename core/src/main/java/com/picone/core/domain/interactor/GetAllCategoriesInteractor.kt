package com.picone.core.domain.interactor

import com.picone.core.data.category.CategoryRepository
import com.picone.core.domain.entity.Category
import javax.inject.Inject

class GetAllCategoriesInteractor @Inject constructor(private val categoryRepository: CategoryRepository){

    fun getAllCategories():List<Category>{
        return categoryRepository.getAllCategories()
    }
}