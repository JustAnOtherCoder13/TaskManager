package com.picone.taskmanager.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Category
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val addNewCategoryInteractor: AddNewCategoryInteractor
):ViewModel() {

    fun setAllCategories()= viewModelScope.launch {
        getAllCategories.value=getAllCategoriesInteractor.getAllCategoriesInteractor()
        Log.i("TAG", "setAllCategories: ${getAllCategories.value!!.size}")
    }

    var getAllCategories = MutableLiveData<List<Category>>()
}