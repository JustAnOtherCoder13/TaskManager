package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
   private val getAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val addNewCategoryInteractor: AddNewCategoryInteractor,
     var ioScope_: CoroutineScope?
):BaseViewModel() {

    init {
        if (ioScope_==null)ioScope_=ioScope
    }
    var allCategoriesMutableLD: MutableLiveData<List<Category>> = MutableLiveData()

    fun allCategories() : LiveData<List<Category>> {
        return getAllCategoriesInteractor.allCategoriesFlow.asLiveData()
    }

    fun getAllCategories(){
        ioScope.launch {
            getAllCategoriesInteractor.allCategoriesFlow
                .collect {
                    allCategoriesMutableLD.value=it
                }
        }
    }

    fun addNewCategory(category: Category){
        ioScope.launch {
            addNewCategoryInteractor.addNewCategory(category)
        }
    }
}