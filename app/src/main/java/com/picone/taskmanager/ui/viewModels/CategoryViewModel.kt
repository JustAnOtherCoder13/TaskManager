package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.picone.core.domain.entity.Category
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    getAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val addNewCategoryInteractor: AddNewCategoryInteractor
):ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)


    val allCategories : LiveData<List<Category>> = getAllCategoriesInteractor.allCategoriesFlow.asLiveData()

    fun addNewCategory(category: Category){
        scope.launch {
            addNewCategoryInteractor.addNewCategory(category)
        }
    }
}