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
    private val mGetAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val mAddNewCategoryInteractor: AddNewCategoryInteractor,
) : BaseViewModel() {

    var mAllCategoriesMutableLD: MutableLiveData<MutableList<Category>> = MutableLiveData()

    fun getAllCategories() {
        viewModelScope.launch {
            mGetAllCategoriesInteractor.allCategoriesFlow
                .collect {
                    mAllCategoriesMutableLD.value = it.toMutableList()
                }
        }
    }

    fun addNewCategory(category: Category) {
        viewModelScope.launch {
            mAddNewCategoryInteractor.addNewCategory(category)
        }
    }
}