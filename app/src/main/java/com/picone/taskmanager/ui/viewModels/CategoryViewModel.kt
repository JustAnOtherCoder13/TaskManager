package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.picone.core.domain.interactor.GetAllCategoriesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllCategoriesInteractor: GetAllCategoriesInteractor
):ViewModel() {

    fun getAllCategories(){
        getAllCategoriesInteractor.getAllCategories()
    }
}