package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Category
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val addNewCategoryInteractor: AddNewCategoryInteractor,
) : BaseViewModel() {

    var allCategoriesMutableLD: MutableLiveData<MutableList<Category>> = MutableLiveData()

    fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesInteractor.allCategoriesFlow
                .collect {
                    allCategoriesMutableLD.value = it.toMutableList()
                }
        }
    }

    fun addNewCategory(category: Category) {
        viewModelScope.launch {
            addNewCategoryInteractor.addNewCategory(category)
        }
    }
}