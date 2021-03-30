package com.picone.taskmanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picone.core.data.Generator
import com.picone.core.data.category.CategoryRepository
import com.picone.core.data.project.ProjectRepository
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import com.picone.taskmanager.ui.viewModels.ProjectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

open class BaseUnitTest {

    companion object {
        private const val TEST_CATEGORY_ID = 3
        private const val TEST_CATEGORY_COLOR = "testColor"
        const val TEST_CATEGORY_NAME = "test"
        val categoryToAdd = Category(TEST_CATEGORY_ID, TEST_CATEGORY_COLOR, TEST_CATEGORY_NAME)

        const val TEST_PROJECT_ID = 4
        const val TEST_PROJECT_CATEGORY_ID = 2
        const val TEST_PROJECT_NAME = "new project"
        const val TEST_PROJECT_DESCRIPTION = "new project description"
        val projectToAdd = Project(
            TEST_PROJECT_ID, TEST_PROJECT_CATEGORY_ID, TEST_PROJECT_NAME,
            TEST_PROJECT_DESCRIPTION
        )
    }

    @get:Rule
    var testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    //CATEGORY VIEW MODEL________________________________________________


    lateinit var categoryViewModel: CategoryViewModel

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @InjectMocks
    private lateinit var getAllCategoriesInteractor: GetAllCategoriesInteractor

    @InjectMocks
    private lateinit var addNewCategoryInteractor: AddNewCategoryInteractor

    @Mock
    private lateinit var categoryObserver: Observer<List<Category>>

    //PROJECT VIEW MODEL__________________________________________________

    lateinit var projectViewModel: ProjectViewModel

    @Mock
    private lateinit var projectRepository: ProjectRepository

    @InjectMocks
    private lateinit var getAllProjectInteractor: GetAllProjectInteractor

    @InjectMocks
    private lateinit var getProjectForIdInteractor: GetProjectForIdInteractor

    @InjectMocks
    private lateinit var addNewProjectInteractor: AddNewProjectInteractor

    @Mock
    private lateinit var allProjectsObserver: Observer<List<Project>>

    @Mock
    private lateinit var projectForIdObserver: Observer<Project>

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        //COMMON----------------------------------------------------------------------------------
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        //CATEGORY VIEW MODEL---------------------------------------------------------------------
        categoryViewModel =
            CategoryViewModel(getAllCategoriesInteractor, addNewCategoryInteractor)
        categoryViewModel.mAllCategoriesMutableLD.observeForever(categoryObserver)
        //stub return------------------------------------------------------
        Mockito.`when`(categoryRepository.getAllCategories())
            .thenReturn(flowOf(Generator.generatedCategories()))

        runBlocking {
            Mockito.`when`(categoryRepository.addNewCategory(categoryToAdd))
                .then { categoryViewModel.mAllCategoriesMutableLD.value?.add(categoryToAdd) }
        }
        categoryViewModel.getAllCategories()//init live data

        //PROJECT VIEW MODEL -------------------------------------------------------------------
        projectViewModel =
            ProjectViewModel(
                getAllProjectInteractor,
                getProjectForIdInteractor,
                addNewProjectInteractor
            )
        projectViewModel.mAllProjectsMutableLD.observeForever(allProjectsObserver)
        projectViewModel.mProjectForIdMutableLD.observeForever(projectForIdObserver)

        //stub return------------------------------------------------------
        Mockito.`when`(projectRepository.getAllProjects())
            .thenReturn(flowOf(Generator.generatedProjects()))

        runBlocking {
            Mockito.`when`(projectRepository.getProjectForId(Generator.generatedProjects()[0].id))
                .thenReturn(Generator.generatedProjects()[0])
        }

        runBlocking {
            Mockito.`when`(projectRepository.addNewProject(projectToAdd))
                .then { projectViewModel.mAllProjectsMutableLD.value?.add(projectToAdd) }
        }
        projectViewModel.getAllProject()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

}