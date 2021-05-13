package com.picone.appcompose.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.picone.appcompose.ui.component.screen.AddScreen
import com.picone.appcompose.ui.component.screen.DetailScreen
import com.picone.appcompose.ui.component.screen.HomeScreen
import com.picone.appcompose.ui.component.screen.Project
import com.picone.appcompose.ui.navigation.MainDestinations.ADD
import com.picone.appcompose.ui.navigation.MainDestinations.DETAIL
import com.picone.appcompose.ui.navigation.MainDestinations.HOME
import com.picone.appcompose.ui.navigation.MainDestinations.PROJECT
import com.picone.appcompose.ui.navigation.navigateToHome
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.Project
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.TASK_ID
import com.picone.core.util.Constants.UnknownTask
import com.picone.viewmodels.*
import com.picone.viewmodels.BaseViewModel.Companion.completionStateMutableLD
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val projectViewModel: ProjectViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val underStainViewModel: UnderStainViewModel by viewModels()
    private val requireActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.getAllTasks()
        projectViewModel.getAllProject()
        categoryViewModel.getAllCategories()

        setContent {
            TaskManagerTheme {
                val allTasks: List<CompleteTask> by taskViewModel.mAllTasksMutableLD.observeAsState(
                    listOf()
                )
                val allProjects: List<Project> by projectViewModel.mAllProjectsMutableLD.observeAsState(
                    listOf()
                )
                val allCategories: List<Category> by categoryViewModel.mAllCategoriesMutableLD.observeAsState(
                    listOf()
                )
                val navController = rememberNavController()

                CheckCompletionStateToNavigate(navController)

                NavHost(navController = navController, startDestination = HOME) {
                    composable(HOME) { HomeScreen(allTasks, navController = navController) }
                    composable(
                        "$DETAIL/{$TASK_ID}",
                        arguments = listOf(navArgument(TASK_ID) { type = NavType.IntType })
                    ) { backStackEntry ->
                        val taskId: Int = backStackEntry.arguments?.getInt(TASK_ID) ?: 0
                        DetailScreen(
                            task = taskToPass(allTasks, taskId),
                            navController = navController
                        )
                    }
                    composable("$ADD/{itemType}")
                    { backStackEntry ->
                        val itemType: String = backStackEntry.arguments?.getString("itemType") ?: ""
                        AddScreen(requireActivity = requireActivity,
                            itemType = itemType,
                            taskId = allTasks.size + 1,
                            allCategories = allCategories,
                            projectId = allProjects.size + 1,
                            addNewProjectOnOkButtonClicked = { projectViewModel.addNewProject(it) },
                            addNewTaskOnOkButtonClicked = {
                                if (it.importance == 0) it.importance = 3
                                taskViewModel.addNewTask(it)
                            }
                        )
                    }
                    composable(PROJECT){ Project(allProjects = allProjects,navController = navController)}
                }
            }
        }
    }

    @Composable
    private fun CheckCompletionStateToNavigate(navController: NavHostController) {
        completionStateMutableLD.observe(this) {
            when (it) {
                BaseViewModel.Companion.CompletionState.TASK_ON_COMPLETE ->
                    navigateToHome(navController = navController)
                else -> {
                }
            }
        }
    }

    private fun taskToPass(allTasks: List<CompleteTask>, taskId: Int): CompleteTask {
        var taskToPass = CompleteTask(UnknownTask)
        if (allTasks.any { it.task.id == taskId }) {
            taskToPass = allTasks.filter { it.task.id == taskId }[FIRST_ELEMENT]
        }
        return taskToPass
    }
}