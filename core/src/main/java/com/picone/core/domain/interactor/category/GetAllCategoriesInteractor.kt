package com.picone.core.domain.interactor.category

import com.picone.core.data.category.CategoryRepository
import com.picone.core.domain.entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesInteractor @Inject constructor(private val categoryRepository: CategoryRepository){

    val allCategoriesFlow : Flow<List<Category>>
    get() = categoryRepository.getAllCategories()
}