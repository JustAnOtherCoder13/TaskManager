package com.picone.taskmanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picone.core.data.Generator
import com.picone.core.data.category.CategoryRepository
import com.picone.core.data.project.ProjectRepository
import com.picone.core.data.task.TaskRepository
import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksForCategoryIdInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.GetTaskForIdInteractor
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import com.picone.taskmanager.ui.viewModels.ProjectViewModel
import com.picone.taskmanager.ui.viewModels.TaskViewModel
import com.picone.taskmanager.ui.viewModels.UnderStainViewModel
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

open class BaseViewModelUnitTest {

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

        const val TEST_TASK_ID = 4
        const val TEST_TASK_CATEGORY_ID = 2
        const val TEST_TASK_NAME = "new task"
        const val TEST_TASK_DESCRIPTION = "new task description"
        const val TEST_TASK_ENTER_DATE = 0
        const val TEST_TASK_END_DATE = 1
        val taskToAdd = Task(
            TEST_TASK_ID, TEST_TASK_CATEGORY_ID, TEST_TASK_NAME, TEST_TASK_DESCRIPTION,
            TEST_TASK_ENTER_DATE, TEST_TASK_END_DATE
        )

        const val TEST_UNDER_STAIN_ID = 4
        const val TEST_UNDER_STAIN_TASK_ID = 1
        const val TEST_UNDER_STAIN_NAME = "new under stain"
        const val TEST_UNDER_STAIN_DESCRIPTION = "new under stain description"
        const val TEST_UNDER_STAIN_ENTER_DATE = 0
        const val TEST_UNDER_STAIN_END_DATE = 1
        val underStainToAdd = UnderStain(
            TEST_UNDER_STAIN_ID, TEST_UNDER_STAIN_TASK_ID, TEST_UNDER_STAIN_NAME,
            TEST_UNDER_STAIN_DESCRIPTION, TEST_UNDER_STAIN_ENTER_DATE, TEST_UNDER_STAIN_END_DATE
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

    //TASK VIEW MODEL__________________________________________________

    lateinit var taskViewModel: TaskViewModel

    @Mock
    private lateinit var taskRepository: TaskRepository

    @InjectMocks
    private lateinit var getAllTasksInteractor: GetAllTasksInteractor

    @InjectMocks
    private lateinit var getTaskForIdInteractor: GetTaskForIdInteractor

    @InjectMocks
    private lateinit var getTasksForCategoryInteractor: GetAllTasksForCategoryIdInteractor

    @InjectMocks
    private lateinit var addNewTaskInteractor: AddNewTaskInteractor

    @Mock
    private lateinit var allTasksObserver: Observer<List<Task>>

    @Mock
    private lateinit var taskForIdObserver: Observer<Task>

    @Mock
    private lateinit var tasksForCategoryObserver: Observer<List<Task>>

    //UNDER STAIN VIEW MODEL__________________________________________________

    lateinit var underStainViewModel: UnderStainViewModel

    @Mock
    private lateinit var underStainRepository: UnderStainRepository

    @InjectMocks
    private lateinit var getAllUnderStainsForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor

    @InjectMocks
    private lateinit var addNewUnderStainInteractor: AddNewUnderStainInteractor

    @Mock
    private lateinit var allUnderStainsForTaskObserver: Observer<List<UnderStain>>

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

        //TASK VIEW MODEL -------------------------------------------------------------------

        taskViewModel = TaskViewModel(
            getAllTasksInteractor,
            getTaskForIdInteractor,
            getTasksForCategoryInteractor,
            addNewTaskInteractor
        )
        taskViewModel.mAllTasksMutableLD.observeForever(allTasksObserver)
        taskViewModel.mTaskForIdMutableLD.observeForever(taskForIdObserver)
        taskViewModel.mTasksForCategoryMutableLD.observeForever(tasksForCategoryObserver)

        Mockito.`when`(taskRepository.getAllTasks()).thenReturn(flowOf(Generator.generatedTasks()))
        runBlocking {
            Mockito.`when`(taskRepository.getAllTasksForCategoryId(Generator.generatedCategories()[0].id))
                .thenReturn(flowOf(Generator.generatedTasks().filter {
                    Generator.generatedTasks()[0].categoryId == it.categoryId
                }))
        }
        runBlocking {
            Mockito.`when`(taskRepository.getTaskForId(Generator.generatedTasks()[0].id))
                .thenReturn(Generator.generatedTasks()[0])
        }
        runBlocking {
            Mockito.`when`(taskRepository.addNewTask(taskToAdd))
                .then { taskViewModel.mAllTasksMutableLD.value?.add(taskToAdd) }
        }

        taskViewModel.getAllTasks()

        //UNDER STAIN VIEW MODEL -------------------------------------------------------------------

        underStainViewModel =
            UnderStainViewModel(getAllUnderStainsForTaskIdInteractor, addNewUnderStainInteractor)

        underStainViewModel.mAllUnderStainsForTaskMutableLD.observeForever(allUnderStainsForTaskObserver)

        Mockito.`when`(underStainRepository.getAllUnderStainForTaskId(Generator.generatedTasks()[0].id))
            .thenReturn(flowOf(Generator.generatedUnderStains().filter {
                it.taskId == Generator.generatedTasks()[0].id
            }))

        runBlocking {
            Mockito.`when`(underStainViewModel.addNewUnderStain(underStainToAdd))
                .then {
                    underStainViewModel.mAllUnderStainsForTaskMutableLD.value?.add(underStainToAdd)
                }
        }
        underStainViewModel.getAllUnderStainsForTask(Generator.generatedTasks()[0])

    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

}