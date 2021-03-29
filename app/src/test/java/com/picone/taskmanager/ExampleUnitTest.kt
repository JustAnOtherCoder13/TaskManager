package com.picone.taskmanager


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picone.core.data.Generator
import com.picone.core.data.category.CategoryRepository
import com.picone.core.domain.entity.Category
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ExampleUnitTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    lateinit var categoryViewModel: CategoryViewModel

    @Mock
    private lateinit var categoryRepository: CategoryRepository
    @InjectMocks
    private lateinit var getAllCategoriesInteractor: GetAllCategoriesInteractor
    @InjectMocks
    private lateinit var addNewCategoryInteractor: AddNewCategoryInteractor

    @Mock
    private lateinit var categoryObserver : Observer<List<Category>>

    private val scope : CoroutineScope = CoroutineScope(Dispatchers.Main)

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        categoryViewModel = CategoryViewModel(getAllCategoriesInteractor,addNewCategoryInteractor,scope)

        categoryViewModel.allCategoriesMutableLD.observeForever(categoryObserver)

        Mockito.`when`(categoryRepository.getAllCategories())
            .thenReturn(flow{Generator.generatedCategories()})



    }

    @Test
    fun assertNotNull() {
        assertNotNull(categoryViewModel)
        assertTrue(categoryViewModel.allCategoriesMutableLD.hasObservers())
        assertNotNull(categoryViewModel.allCategoriesMutableLD)
    }

    @Test
    fun getAllCategoriesShouldReturnListOfCategories(){
        assertNull(categoryViewModel.allCategoriesMutableLD.value)
        categoryViewModel.getAllCategories()
        assertEquals(Generator.generatedCategories().size,
            categoryViewModel.allCategoriesMutableLD.value?.size
        )
    }
}