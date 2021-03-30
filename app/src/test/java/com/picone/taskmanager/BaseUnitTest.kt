package com.picone.taskmanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picone.core.data.Generator
import com.picone.core.data.category.CategoryRepository
import com.picone.core.domain.entity.Category
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

open class BaseUnitTest {

    @get:Rule
    var testInstantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    //CATEGORY VIEW MODEL
    private val TEST_ID: Int = 3
    private val TEST_COLOR: String = "testColor"
    val TEST_NAME: String = "test"
    val categoryToAdd: Category = Category(TEST_ID, TEST_COLOR, TEST_NAME)

    lateinit var categoryViewModel: CategoryViewModel
    @Mock
    private lateinit var categoryRepository: CategoryRepository
    @InjectMocks
    private lateinit var getAllCategoriesInteractor: GetAllCategoriesInteractor
    @InjectMocks
    private lateinit var addNewCategoryInteractor: AddNewCategoryInteractor
    @Mock
    private lateinit var categoryObserver: Observer<List<Category>>
    //

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        //COMMON----------------------------------------------------------------------------------
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        //CATEGORY VIEW MODEL---------------------------------------------------------------------
        categoryViewModel =
            CategoryViewModel(getAllCategoriesInteractor, addNewCategoryInteractor)
        categoryViewModel.allCategoriesMutableLD.observeForever(categoryObserver)
            //stub return------------------------------------------------------
        Mockito.`when`(categoryRepository.getAllCategories())
            .thenReturn(flowOf(Generator.generatedCategories()))

        runBlocking {
            Mockito.`when`(categoryRepository.addNewCategory(categoryToAdd))
                .then { categoryViewModel.allCategoriesMutableLD.value?.add(categoryToAdd) }
        }
        categoryViewModel.getAllCategories()//init live data

        //
    }

}